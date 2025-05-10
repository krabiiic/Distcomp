#include "KafkaConsumer.hpp"
#include <algorithm>
#include <iostream>
#include <regex>

KafkaConsumer::~KafkaConsumer() {
    if (m_topic_partition) {
        rd_kafka_topic_partition_list_destroy(m_topic_partition);
    }
    if (m_handle) {
        rd_kafka_consumer_close(m_handle);
        rd_kafka_destroy(m_handle);
    }
    if (m_conf) {
        rd_kafka_conf_destroy(m_conf);
    }
}

bool KafkaConsumer::initialize(const std::string& address, const std::string& topic) {
    char errstr[512];
    m_conf = rd_kafka_conf_new();
    
    if (rd_kafka_conf_set(m_conf, "bootstrap.servers", address.c_str(),
                         errstr, sizeof(errstr)) != RD_KAFKA_CONF_OK) {
        std::cerr << "Failed to set brokers: " << errstr << std::endl;
        return false;
    }

    if (rd_kafka_conf_set(m_conf, "group.id", "my_consumer_group",
                         errstr, sizeof(errstr)) != RD_KAFKA_CONF_OK) {
        std::cerr << "Failed to set group.id: " << errstr << std::endl;
        return false;
    }

    m_handle = rd_kafka_new(RD_KAFKA_CONSUMER, m_conf, errstr, sizeof(errstr));
    if (!m_handle) {
        std::cerr << "Failed to create consumer: " << errstr << std::endl;
        return false;
    }
    
    m_conf = nullptr; 
    m_topic_partition = rd_kafka_topic_partition_list_new(1);
    rd_kafka_topic_partition_list_add(m_topic_partition, topic.c_str(), RD_KAFKA_PARTITION_UA);
    
    if (rd_kafka_subscribe(m_handle, m_topic_partition)) {
        std::cerr << "Failed to subscribe to topic" << std::endl;
        return false;
    }
    
    return true;
}

std::optional<nlohmann::json> KafkaConsumer::consume(int timeout_ms) {
    rd_kafka_message_t* msg = rd_kafka_consumer_poll(m_handle, timeout_ms);
    if (!msg) return std::nullopt;
    
    if (msg->err) {
        std::cerr << "Consumer error: " << rd_kafka_message_errstr(msg) << std::endl;
        rd_kafka_message_destroy(msg);
        return std::nullopt;
    }

    try {
        std::string payload_str(static_cast<const char*>(msg->payload), msg->len);

        if (payload_str.empty()) {
            std::cerr << "Received empty message!" << std::endl;
            rd_kafka_message_destroy(msg);
            return std::nullopt;
        }

        auto json = nlohmann::json::parse(payload_str);
        rd_kafka_message_destroy(msg);
        return json;
        
    } catch (const std::exception& e) {
        std::cerr << "JSON parse error: " << e.what() << std::endl;
        rd_kafka_message_destroy(msg);
        return std::nullopt;
    }
}
