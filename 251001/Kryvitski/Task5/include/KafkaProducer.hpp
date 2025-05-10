#pragma once
#include <librdkafka/rdkafka.h>
#include <nlohmann/json.hpp>
#include <optional>
#include <string>

class KafkaProducer {
public:
    KafkaProducer() noexcept = default;
    virtual ~KafkaProducer();
    
    bool initialize(const std::string& brokers, const std::string& topic);
    bool sendMessage(const nlohmann::json& message);

private:
    rd_kafka_t* m_handle;
    rd_kafka_conf_t* m_conf;
    rd_kafka_topic_t* m_topic;
    std::string m_topic_name;
};