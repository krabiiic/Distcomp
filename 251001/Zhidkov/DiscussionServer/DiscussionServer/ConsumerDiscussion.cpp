#include <librdkafka/rdkafka.h>
#include <crow.h>
#include <optional>
#include <nlohmann/json.hpp>
#include "ConsumerDiscussion.h"
#include "CurlHttpClient.h"
#include "ProducerDiscussion.h"

using json = nlohmann::json;
std::optional<int> jsonToId(const json& j1) {
    if (j1.contains("id") && j1["id"].is_number_integer()) {
        return j1["id"].get<int>();
    }
    return std::nullopt;
}

crow::request jsonToRequest(const json& j1) {
    crow::request req;
    json j;
    // Проверяем, есть ли ключ "req"
    if (!j1.contains("req")) {
        j = j1;
    }
    else {
        j = j1["req"];
    }

    // Проверка метода запроса
    if (j.contains("method") && j["method"].is_number()) {
        if (j["method"] == crow::HTTPMethod::Get) req.method = crow::HTTPMethod::Get;
        else if (j["method"] == crow::HTTPMethod::Delete) req.method = crow::HTTPMethod::Delete;
        else if (j["method"] == crow::HTTPMethod::Put) req.method = crow::HTTPMethod::Put;
        else if (j["method"] == crow::HTTPMethod::Post) req.method = crow::HTTPMethod::Post;
    }

    // Заполняем URL, если есть
    if (j.contains("raw_url") && j["raw_url"].is_string()) req.raw_url = j["raw_url"].get<std::string>();
    if (j.contains("url") && j["url"].is_string()) req.url = j["url"].get<std::string>();

    // Разбор query-параметров
    if (j.contains("query_params") && j["query_params"].is_object()) {
        std::string query_string;
        for (auto& [key, value] : j["query_params"].items()) {
            if (!query_string.empty()) query_string += "&";
            if (value.is_string()) query_string += key + "=" + value.get<std::string>();
        }
        req.url_params = crow::query_string(query_string);
    }

    // Разбор заголовков
    if (j.contains("headers") && j["headers"].is_object()) {
        for (auto& [key, value] : j["headers"].items()) {
            if (value.is_string()) req.headers.insert({ key, value.get<std::string>() });
        }
    }

    // Тело запроса
    if (j.contains("body") && j["body"].is_string()) req.body = j["body"].get<std::string>();
    if (j.contains("remote_ip_address") && j["remote_ip_address"].is_string()) req.remote_ip_address = j["remote_ip_address"].get<std::string>();

    // HTTP-версия
    if (j.contains("http_ver_major") && j["http_ver_major"].is_number_integer()) req.http_ver_major = j["http_ver_major"].get<int>();
    if (j.contains("http_ver_minor") && j["http_ver_minor"].is_number_integer()) req.http_ver_minor = j["http_ver_minor"].get<int>();

    // Флаги соединения
    if (j.contains("keep_alive") && j["keep_alive"].is_boolean()) req.keep_alive = j["keep_alive"].get<bool>();
    if (j.contains("close_connection") && j["close_connection"].is_boolean()) req.close_connection = j["close_connection"].get<bool>();
    if (j.contains("upgrade") && j["upgrade"].is_boolean()) req.upgrade = j["upgrade"].get<bool>();

    return req;
}

std::mutex ConsumerDiscussion::queueMutex;
std::queue<std::string> ConsumerDiscussion::responseQueue;
std::condition_variable ConsumerDiscussion::responseCondition;

void ConsumerDiscussion::consumeInTopic()
{
    rd_kafka_conf_t* conf = rd_kafka_conf_new();
    rd_kafka_conf_set(conf, "bootstrap.servers", "localhost:9092", nullptr, 0);
    rd_kafka_conf_set(conf, "group.id", "response_consumer", nullptr, 0);
    rd_kafka_conf_set(conf, "auto.offset.reset", "earliest", nullptr, 0);

    rd_kafka_t* consumer = rd_kafka_new(RD_KAFKA_CONSUMER, conf, nullptr, 0);
    rd_kafka_poll_set_consumer(consumer);

    rd_kafka_topic_partition_list_t* topics = rd_kafka_topic_partition_list_new(1);
    rd_kafka_topic_partition_list_add(topics, "InTopic", RD_KAFKA_PARTITION_UA);
    rd_kafka_subscribe(consumer, topics);

    while (true) {
        rd_kafka_message_t* msg = rd_kafka_consumer_poll(consumer, 100);
        if (msg) {
            std::string response = std::string((char*)msg->payload, msg->len);
            if (response.size() > 0) {
                // Добавляем ответ в очередь
                {
                    std::lock_guard<std::mutex> lock(queueMutex);
                    responseQueue.push(response);
                    crow::request req = jsonToRequest(json::parse(response));
                    json j = json::parse(response);
                    if (req.method == crow::HTTPMethod::Get) {
                        CurlHttpClient client;
                        if (j.contains("id")) {
                            int id = static_cast<int>(j["id"]);
                            client.getById("http://localhost:24130/api/v1.0/notices", id);
                        }
                        else {
                            client.get("http://localhost:24130/api/v1.0/notices");
                        }
                    }
                    if (req.method == crow::HTTPMethod::Delete) {
                        CurlHttpClient client;
                        int id = static_cast<int>(j["id"]);
                        client.deleteRequest("http://localhost:24130/api/v1.0/notices", id);
                    }
                    if (req.method == crow::HTTPMethod::Post) {
                        CurlHttpClient client;
                        client.post("http://localhost:24130/api/v1.0/notices", req.body);
                    }
                    if (req.method == crow::HTTPMethod::Put) {
                        CurlHttpClient client;
                        int id = static_cast<int>(j["id"]);
                        client.put("http://localhost:24130/api/v1.0/notices", id, req.body);
                    }

                }

                // Уведомляем главный поток, что ответ пришел
                responseCondition.notify_one();
            }

            rd_kafka_message_destroy(msg);
        }
    }

    rd_kafka_consumer_close(consumer);
    rd_kafka_destroy(consumer);
}

void ConsumerDiscussion::awaitResponseFromInTopic()
{
    while (true) {
        std::unique_lock<std::mutex> lock(queueMutex);

        // Ожидаем, пока в очереди не появится ответ
        responseCondition.wait(lock, [] { return !responseQueue.empty(); });

        // Получаем первый ответ из очереди
        std::string response = responseQueue.front();
        //ProducerDiscussion::sendToKafka("OutTopic", response);
        responseQueue.pop();
    }
    //return response;
}
