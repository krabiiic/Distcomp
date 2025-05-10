#pragma once
#include <librdkafka/rdkafka.h>
#include <nlohmann/json.hpp>
#include <optional>
#include <string>

class KafkaConsumer {
public:
    KafkaConsumer() noexcept = default;
    virtual ~KafkaConsumer();
    
    bool initialize(const std::string& address, const std::string& topic);
    std::optional<nlohmann::json> consume(int timeout_ms);

private:
    rd_kafka_t* m_handle;
    rd_kafka_conf_t* m_conf;
    rd_kafka_topic_partition_list_t* m_topic_partition;
};