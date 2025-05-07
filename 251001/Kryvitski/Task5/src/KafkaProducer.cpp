#include "KafkaProducer.hpp"
#include <iostream>

KafkaProducer::~KafkaProducer() {
    if (m_topic) {
        rd_kafka_topic_destroy(m_topic);
    }
    if (m_handle) {
        rd_kafka_flush(m_handle, 1000);
        rd_kafka_destroy(m_handle);
    }
    if (m_conf) {
        rd_kafka_conf_destroy(m_conf);
    }
}

bool KafkaProducer::initialize(const std::string& brokers, const std::string& topic) {
    char errstr[512];
    m_conf = rd_kafka_conf_new();
    
    if (rd_kafka_conf_set(m_conf, "bootstrap.servers", brokers.c_str(),
                         errstr, sizeof(errstr)) != RD_KAFKA_CONF_OK) {
        std::cerr << "Failed to set brokers: " << errstr << std::endl;
        return false;
    }

    rd_kafka_conf_set(m_conf, "compression.codec", "none", errstr, sizeof(errstr));

    m_handle = rd_kafka_new(RD_KAFKA_PRODUCER, m_conf, errstr, sizeof(errstr));
    if (!m_handle) {
        std::cerr << "Failed to create producer: " << errstr << std::endl;
        return false;
    }
    
    m_conf = nullptr; 
    m_topic_name = topic;
    
    m_topic = rd_kafka_topic_new(m_handle, m_topic_name.c_str(), nullptr);
    if (!m_topic) {
        std::cerr << "Failed to create topic: " << rd_kafka_err2str(rd_kafka_last_error()) << std::endl;
        return false;
    }
    
    return true;
}

bool KafkaProducer::sendMessage(const nlohmann::json& message) {
    std::string msg_str = message.dump();

    if (rd_kafka_produce(
            m_topic,
            RD_KAFKA_PARTITION_UA,
            RD_KAFKA_MSG_F_COPY,
            (void*)msg_str.c_str(),
            msg_str.size(),
            nullptr, 0, nullptr) != 0) {
        std::cerr << "Failed to produce message: " 
                 << rd_kafka_err2str(rd_kafka_last_error()) << std::endl;
        return false;
    }
    rd_kafka_poll(m_handle, 0);
    return true;
}