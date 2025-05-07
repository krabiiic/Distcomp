#pragma once
#include "PublisherHandler.hpp"
#include "KafkaProducer.hpp"
#include "KafkaConsumer.hpp"
#include <memory>
#include <chrono>
#include <nlohmann/json.hpp>

using namespace std::chrono_literals;

template<PostgresEntity ... Ps>
class PublisherServer final{
public:
    PublisherServer();

    template<CassandraEntity ... Cs>
    void redirect_to_discussion();
    void start_server();

private:
    template<PostgresEntity T>
    void register_entity();

    template<CassandraEntity C>
    void register_discussion_entity();

    std::optional<nlohmann::json> get_kafka_response(const std::string& correlation_id);

private:
    constexpr static auto KAFKA_ADDRESS = "127.0.0.1:9092";
    constexpr static auto SERVER_ADDRESS = "172.17.0.1";
    constexpr static uint16_t SERVER_PORT = 24110;

    httplib::Server m_server;
    KafkaProducer m_producer;
    KafkaConsumer m_consumer;
    std::shared_ptr<PostgresController> m_controller;
};


template<PostgresEntity ... Ps>
PublisherServer<Ps...>::PublisherServer() {
    m_controller = std::make_shared<PostgresController>();
    m_controller->initialize();
    m_producer.initialize(KAFKA_ADDRESS, "in");
    m_consumer.initialize(KAFKA_ADDRESS, "out");
    (register_entity<Ps>(), ...);
}

template<PostgresEntity ... Ps>
template<PostgresEntity T>
void PublisherServer<Ps...>::register_entity() {
    auto handler = std::make_shared<PublisherHandler<T>>(m_controller);
    handler->initialize();
    std::string entity_name = T::entity_name;
    std::string path = "/api/v1.0/" + entity_name;

    m_server.Post(path, [handler](const auto& req, auto& res) {
        handler->handle_post(req, res);
    });

    m_server.Get(path, [handler](const auto& req, auto& res) {
        handler->handle_get_all(req, res);
    });

    m_server.Get(path + "/(\\d+)", [handler](const auto& req, auto& res) {
        uint64_t id = std::stoull(req.matches[1]);
        handler->handle_get_one(req, res, id);
    });

    m_server.Delete(path + "/(\\d+)", [handler](const auto& req, auto& res) {
        uint64_t id = std::stoull(req.matches[1]);
        handler->handle_delete(req, res, id);
    });

    m_server.Put(path, [handler](const auto& req, auto& res) {
        handler->handle_put(req, res);
    });
}

template<PostgresEntity ... Ps>
template<CassandraEntity ... Cs>
void PublisherServer<Ps...>::redirect_to_discussion() { 
    (register_discussion_entity<Cs>(), ...);
}

template<PostgresEntity ... Ps>
template<CassandraEntity C>
void PublisherServer<Ps...>::register_discussion_entity(){
    const std::string path = C::entity_name;
    const std::string full_path = "/api/v1.0/" + path;
    
    auto create_handler = [this, &path](const std::string& method, bool has_id = false) {
        return [this, method, has_id, path](const httplib::Request& req, httplib::Response& res) {
            std::string correlation_id = std::to_string(
                std::chrono::system_clock::now().time_since_epoch().count());
            
            nlohmann::json message;
            message["method"] = method;
            message["entity"] = path;
            message["correlation_id"] = correlation_id;
            
            if (method == "POST" || method == "PUT") {
                message["body"] = json::parse(req.body);
            }
            
            if (has_id) {
                message["path_id"] = std::stoull(req.matches[1]);
            }
            
            if (!m_producer.sendMessage(message)) {
                res.status = 500;
                res.set_content("Failed to send to Kafka", "text/plain");
                return;
            }
            
            auto response = get_kafka_response(correlation_id);
            if (response.has_value()) {
                auto json = response.value();
                res.status = json["status"];
                if (json.contains("data")) {
                    res.set_content(json["data"].dump(), "application/json");
                }
            } else {
                res.status = 404;
                res.set_content("Timeout waiting for response", "text/plain");
            }
        };
    };

    m_server.Post(full_path, create_handler("POST"));
    m_server.Get(full_path, create_handler("GET"));
    m_server.Get(full_path + "/(\\d+)", create_handler("GET", true));
    m_server.Delete(full_path + "/(\\d+)", create_handler("DELETE", true));
    m_server.Put(full_path, create_handler("PUT"));
}

template<PostgresEntity ... Ps>
std::optional<nlohmann::json> PublisherServer<Ps...>::get_kafka_response(
    const std::string& correlation_id) 
{
    auto start = std::chrono::steady_clock::now();
    constexpr auto timeout = 1s;
    static int message_counter = 0;

    while (std::chrono::steady_clock::now() - start < timeout) {
        auto msg = m_consumer.consume(10); 
        if (msg.has_value()) {
            auto json = msg.value();
            try {
                std::string correlation = json["correlation_id"];
                if (correlation == correlation_id) {
                    return json["response"];
                }
            } catch (const std::exception& e) {
                std::cout << "Error processing message: " << e.what() << std::endl;
            }
        }
    }
    return std::nullopt;
}

template<PostgresEntity ... Ps>
void PublisherServer<Ps...>::start_server() {
    m_server.listen(SERVER_ADDRESS, SERVER_PORT);
}
