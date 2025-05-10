#include <librdkafka/rdkafka.h>
#include "ProducerDiscussion.h"

void ProducerDiscussion::sendToKafka(const std::string& topic, const std::string& message)
{
    rd_kafka_conf_t* conf = rd_kafka_conf_new();
    rd_kafka_conf_set(conf, "bootstrap.servers", "localhost:9092", nullptr, 0);

    char errstr[512];
    rd_kafka_t* producer = rd_kafka_new(RD_KAFKA_PRODUCER, conf, errstr, sizeof(errstr));

    rd_kafka_producev(producer,
        RD_KAFKA_V_TOPIC(topic.c_str()),
        RD_KAFKA_V_VALUE(message.c_str(), message.size()),
        RD_KAFKA_V_END);

    rd_kafka_flush(producer, 5000);
    rd_kafka_destroy(producer);
}