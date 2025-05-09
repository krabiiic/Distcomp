#include "nlohmann/json.hpp"
#include "NoticeController.h"
#include "../Services/NoticeService.h"
#include "../CurlHttpClient.h"
#include "../ConsumerPublisher.h"
#include "../ProducerPublisher.h"
#include <hiredis/hiredis.h>

extern redisContext* redis_context;
std::string generate_cache_key_notice(const crow::request& req) {
    // Генерация ключа для кэша на основе метода и тела запроса
    std::string method_str;

    switch (req.method) {
    case crow::HTTPMethod::GET:    method_str = "GET"; break;
    case crow::HTTPMethod::POST:   method_str = "POST"; break;
    case crow::HTTPMethod::PUT:    method_str = "PUT"; break;
    case crow::HTTPMethod::Delete: method_str = "DELETE"; break;
        // Можно добавить другие методы, если нужно
    default: method_str = "UNKNOWN"; break;
    }

    // Генерация ключа
    return method_str + ":" + req.url + ":" + req.body;
}
void delete_related_keys_notice(const std::string& str) {
    //redisCommand(redis_context, "FLUSHALL");
    redisReply* reply = (redisReply*)redisCommand(redis_context, std::string("SCAN 0 MATCH *" + str + "*").c_str());

    if (reply->type == REDIS_REPLY_ARRAY) {
        for (size_t i = 0; i < reply->elements; ++i) {
            if (reply->element[i]->str && reply->element[i]->str != "0") {
                std::string key = reply->element[i]->str;
                redisCommand(redis_context, "DEL %s", key.c_str());
            }
            else {
                if (!reply->element[i]->str) {
                    for (size_t j = 0; j < reply->element[i]->elements; ++j) {
                        if (reply->element[i]->element[j]->str && reply->element[i]->element[j]->str != "0") {
                            std::string key = reply->element[i]->element[j]->str;
                            redisCommand(redis_context, "DEL %s", key.c_str());
                        }
                    }
                }
            }
        }
    }
}
using json = nlohmann::json;
std::atomic<bool> putIsDone = false;
std::atomic<int> createCounter = 0;
std::atomic<int> getAllCounter = 0;
crow::request jsonToRequest(const json& j) {
    crow::request req;

    // Проверяем, есть ли ключ "method" и корректный ли он
    if (j.contains("method") && j["method"].is_string()) {
        std::string method = j["method"].get<std::string>();
        if (method == "GET") req.method = crow::HTTPMethod::GET;
        else if (method == "DELETE") req.method = crow::HTTPMethod::Delete;
        else if (method == "PUT") req.method = crow::HTTPMethod::PUT;
        else if (method == "POST") req.method = crow::HTTPMethod::POST;
    }

    // Заполняем остальные поля, если они есть и нужного типа
    if (j.contains("raw_url") && j["raw_url"].is_string()) req.raw_url = j["raw_url"].get<std::string>();
    if (j.contains("url") && j["url"].is_string()) req.url = j["url"].get<std::string>();

    // Разбор query-параметров (если есть)
    if (j.contains("query_params") && j["query_params"].is_object()) {
        std::string query_string;
        for (auto& [key, value] : j["query_params"].items()) {
            if (!query_string.empty()) query_string += "&";
            if (value.is_string()) query_string += key + "=" + value.get<std::string>();
        }
        req.url_params = crow::query_string(query_string);
    }

    // Разбор заголовков (если есть)
    if (j.contains("headers") && j["headers"].is_object()) {
        for (auto& [key, value] : j["headers"].items()) {
            if (value.is_string()) req.headers.insert({ key, value.get<std::string>() });
        }
    }

    // Остальные параметры
    if (j.contains("body") && j["body"].is_string()) req.body = j["body"].get<std::string>();
    if (j.contains("remote_ip_address") && j["remote_ip_address"].is_string()) req.remote_ip_address = j["remote_ip_address"].get<std::string>();
    if (j.contains("http_ver_major") && j["http_ver_major"].is_number_integer()) req.http_ver_major = j["http_ver_major"].get<int>();
    if (j.contains("http_ver_minor") && j["http_ver_minor"].is_number_integer()) req.http_ver_minor = j["http_ver_minor"].get<int>();
    if (j.contains("keep_alive") && j["keep_alive"].is_boolean()) req.keep_alive = j["keep_alive"].get<bool>();
    if (j.contains("close_connection") && j["close_connection"].is_boolean()) req.close_connection = j["close_connection"].get<bool>();
    if (j.contains("upgrade") && j["upgrade"].is_boolean()) req.upgrade = j["upgrade"].get<bool>();

    return req;
}


json requestToJson(const crow::request& req) {
    json j;

    // Сериализация данных запроса в JSON
    j["method"] = req.method;
    j["raw_url"] = req.raw_url;
    j["url"] = req.url;

    // Query параметры
    json query_params;
    for (const auto& key : req.url_params.keys()) {
        query_params[key] = req.url_params.get(key);
    }
    j["query_params"] = query_params;

    // Заголовки
    json headers;
    for (const auto& header : req.headers) {
        headers[header.first] = header.second;
    }
    j["headers"] = headers;

    // Остальные параметры
    j["body"] = req.body;
    j["remote_ip_address"] = req.remote_ip_address;
    j["http_ver_major"] = req.http_ver_major;
    j["http_ver_minor"] = req.http_ver_minor;
    j["keep_alive"] = req.keep_alive;
    j["close_connection"] = req.close_connection;
    j["upgrade"] = req.upgrade;

    return j;
}

crow::response NoticeController::get_notices(const crow::request& req)
{
    ++counter;
    ++getAllCounter;
    CROW_LOG_INFO << "COUNTER GET ALL: " << counter;
    if (getAllCounter > 1)
        int yyyy = 0;
    CurlHttpClient client;
    std::string str(requestToJson(req).dump());
    ProducerPublisher::sendToKafka("InTopic", str);
    std::string response = ConsumerPublisher::awaitResponseFromOutTopic();//client.get("http://localhost:24130/api/v1.0/notices");
    json j = json::parse(response);
    json jres;
    if (j.contains("body")) {
        jres = json::parse((std::string)j["body"]);
        response = jres.dump();
    }
    return crow::response(response);
    std::vector<Notice> notices = NoticeService::get_all();

    // Формируем JSON массив
    crow::json::wvalue result;
    crow::json::wvalue result1;
    crow::json::wvalue::list notices_list;  // Вектор JSON-объектов

    for (const auto& notice : notices) {
        notices_list.push_back(crow::json::wvalue{
            {"id", notice.id},
            {"newsId", notice.newsId},
            {"content", notice.content},
            });
        std::cout << notice.id << std::endl;
        std::cout << notice.newsId << std::endl;
    }

    //result["users"] = std::move(users_list);  // Записываем массив в JSON-ответ
    if (!notices_list.empty()) {
        result1 = std::move(notices_list.front());
        //result1 = std::move(news_list.front());
    }
    else {
        result["notices"] = std::move(notices_list);
        return crow::response(result);
    }
    //std::cout << result.dump(4) << std::endl;
    std::cout << result1.dump(4) << std::endl;
    //std::cout << users_list.front().dump(4) << std::endl;
    std::cout << "GETALL" << std::endl;

    return crow::response(result1);
}

crow::response NoticeController::get_notice(const crow::request& req, int id)
{
    ++counter;
    CROW_LOG_INFO << "COUNTER GET BY ID: " << counter;
    CurlHttpClient client;
    json j = requestToJson(req);
    CROW_LOG_INFO << "AFTER REQUEST";
    j["id"] = id; 
    std::string str(j.dump());
    ProducerPublisher::sendToKafka("InTopic", str);
    std::string response = ConsumerPublisher::awaitResponseFromOutTopic();
    if (putIsDone.load()) {
        while (response.find("updat") == std::string::npos) {
            response = ConsumerPublisher::awaitResponseFromOutTopic();
        }
        putIsDone.store(false);
    }
    
    CROW_LOG_INFO << "GET RESPONSE";
    //std::string response = client.getById("http://localhost:24130/api/v1.0/notices", id);
    j.clear();
    j = json::parse(response);
    json j1;
    if (j.contains("body") && !std::string(j["body"]).empty())
        j1 = json::parse((std::string)j["body"]);
    crow::json::wvalue res_json;
    //if (j.contains("body")) {
        if (j1.contains("id"))
            res_json["id"] = (int)j1["id"];
        if (j1.contains("newsId"))
            res_json["newsId"] = (int)j1["newsId"];
        if (j1.contains("content") && !std::string(j1["content"]).empty())
            res_json["content"] = (std::string)j1["content"];
    //}
    /*if (putIsDone) {
        int yyy = 0;
        if (j1.contains("hash"))
            yyy = j1["hash"];
        putIsDone.store(false);
    }*/
    return crow::response(res_json);

    std::cout << "READ GET" << std::endl;
    //bool success = read_user_from_db(id);

    auto notice = NoticeService::get_by_id(id);

    if (notice) {
        // Если пользователь найден, возвращаем его в формате JSON
        crow::json::wvalue res_json;
        res_json["id"] = notice->id;
        res_json["newsId"] = notice->newsId;
        res_json["content"] = notice->content;

        return crow::response(200, res_json);
    }
    else {
        // Если пользователь не найден, возвращаем ошибку 404
        return crow::response(404, "Notice not found");
    }
}

crow::response NoticeController::create_notice(const crow::request& req)
{
    ++counter;
    ++createCounter;
    CROW_LOG_INFO << "COUNTER CREATE: " << counter;
    CurlHttpClient client;
    json j = requestToJson(req);
    std::string str(j.dump());
    ProducerPublisher::sendToKafka("InTopic", str);
    if (createCounter > 1)
        int yyyy = 0;
    std::string response = ConsumerPublisher::awaitResponseFromOutTopic();
    while (true) {
        if ((response.find("updat") == std::string::npos && response.find("cassandraDirectWrite") == std::string::npos) && response.find("content") != std::string::npos) {
            break;
        }
        response = ConsumerPublisher::awaitResponseFromOutTopic();
    }
    j = json::parse(response);
    //j.erase("code");
    std::pair<int, std::string> resp;//client.post("http://localhost:24130/api/v1.0/notices", req.body);
    if (j.contains("code"))
        resp.first = j["code"];
    else
        resp.first = 999;
    if (j.contains("body"))
        resp.second = j["body"];
    //resp
    CROW_LOG_INFO << "|||Received body: " << req.body;
    CROW_LOG_INFO << "|||response: " << resp.second;
    CROW_LOG_INFO << "|||code: " << resp.first;
    //if ()
    return crow::response(201, resp.second);

    CROW_LOG_INFO << "Received body: " << req.body;  // Логируем тело запроса

    auto json_data = crow::json::load(req.body);
    if (!json_data) {
        return crow::response(400, R"({"error": "Invalid JSON"})");
    }

    if (!json_data || !json_data.has("content") || !json_data.has("newsId")) {
        return crow::response(400, R"({"error": "Missing required fields"})");
    }

    Notice notice;
    //new_user.id = json_data["id"];
    notice.newsId = json_data["newsId"].i();
    notice.content = json_data["content"].s();

    if (NoticeService::create(notice)) {
        std::optional<int> id = NoticeService::get_id(notice);
        if (!id.has_value()) {
            std::cout << "No value" << std::endl;
            return crow::response(400, R"({"error": "Failed to retrieve user ID"})");
        }

        crow::json::wvalue res_json;
        res_json["id"] = static_cast<int>(id.value());
        res_json["newsId"] = notice.newsId;
        res_json["content"] = notice.content;
        return crow::response(201, res_json);
    }
    else {
        std::cout << "db error" << std::endl;
        return crow::response(400, R"({"error": "Database error"})");
    }
}

crow::response NoticeController::delete_notice(const crow::request& req, int id)
{
    ++counter;
    CROW_LOG_INFO << "COUNTER DELETE: " << counter;
    CurlHttpClient client;
    //ProducerPublisher::sendToKafka("InTopic", req.body);
    std::string response = client.deleteRequest("http://localhost:24130/api/v1.0/notices", id);//ConsumerPublisher::awaitResponseFromOutTopic();//client.deleteRequest("http://localhost:24130/api/v1.0/notices", id);
    return crow::response(response);

    crow::json::wvalue res_json;
    std::cout << "DELETE" << std::endl;
    if (NoticeService::exists(id)) {
        bool success = NoticeService::delete_by_id(id);

        if (success) {
            res_json["status"] = "success";
            res_json["message"] = "Notice deleted successfully";
            return crow::response(204);
        }
        else {
            res_json["status"] = "error";
            res_json["message"] = "Failed to delete news";
            return crow::response(500, res_json);
        }
    }
    else {
        res_json["status"] = "error";
        res_json["message"] = "Notice not found";
        return crow::response(404, res_json);
    }
}

crow::response NoticeController::update_notice(const crow::request& req)
{
    putIsDone.store(true);
    ++counter;
    CROW_LOG_INFO << "COUNTER UPDATE: " << counter;
    CurlHttpClient client;
    auto json_data1 = crow::json::load(req.body);

    if (!json_data1 || !json_data1.has("newsId") || !json_data1.has("id") || !json_data1.has("content")) {
        return crow::response(400, "Invalid JSON");
    }
    // Извлекаем данные из JSON
    int id1 = json_data1["id"].i();
    //ProducerPublisher::sendToKafka("InTopic", req.body);
    std::string response = client.put("http://localhost:24130/api/v1.0/notices", id1, req.body);//ConsumerPublisher::awaitResponseFromOutTopic();//client.put("http://localhost:24130/api/v1.0/notices", id1, req.body);
    CROW_LOG_INFO << "|||Received body: " << req.body;
    CROW_LOG_INFO << "|||response: " << response;
    return crow::response(response);

    crow::json::wvalue res_json;
    std::cout << "PUT" << std::endl;
    // Загружаем данные из тела запроса
    auto json_data = crow::json::load(req.body);

    if (!json_data || !json_data.has("newsId") || !json_data.has("id") || !json_data.has("content")) {
        return crow::response(400, "Invalid JSON");
    }

    // Извлекаем данные из JSON
    int id = json_data["id"].i();
    int newsId = json_data["newsId"].i();
    std::string content = json_data["content"].s();
    //if (user_exists(id)) {
        // Обновление пользователя в базе данных
    bool success = NoticeService::update(id, newsId, content);

    if (success) {
        auto notice = NoticeService::get_by_id(id);

        if (notice) {
            // Если пользователь найден, возвращаем его в формате JSON

            res_json["id"] = notice->id;
            res_json["newsId"] = notice->newsId;
            res_json["content"] = notice->content;

            return crow::response(200, res_json);
        }
        else {
            res_json["status"] = "error";
            res_json["message"] = "Notice not found";
            return crow::response(404, res_json);
        }
    }
    else {
        res_json["status"] = "error";
        res_json["message"] = "Notice not found";
        return crow::response(404, res_json);
    }
}
