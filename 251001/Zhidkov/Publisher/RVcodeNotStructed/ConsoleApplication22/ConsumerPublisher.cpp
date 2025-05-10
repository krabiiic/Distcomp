#include <librdkafka/rdkafka.h>
#include "ConsumerPublisher.h"
std::mutex ConsumerPublisher::queueMutex;
std::queue<std::string> ConsumerPublisher::responseQueue;
std::condition_variable ConsumerPublisher::responseCondition;

void ConsumerPublisher::consumeOutTopic()
{
    rd_kafka_conf_t* conf = rd_kafka_conf_new();
    rd_kafka_conf_set(conf, "bootstrap.servers", "localhost:9092", nullptr, 0);
    rd_kafka_conf_set(conf, "group.id", "response_consumer", nullptr, 0);
    rd_kafka_conf_set(conf, "auto.offset.reset", "earliest", nullptr, 0);

    rd_kafka_t* consumer = rd_kafka_new(RD_KAFKA_CONSUMER, conf, nullptr, 0);
    rd_kafka_poll_set_consumer(consumer);

    rd_kafka_topic_partition_list_t* topics = rd_kafka_topic_partition_list_new(1);
    rd_kafka_topic_partition_list_add(topics, "OutTopic", RD_KAFKA_PARTITION_UA);
    rd_kafka_subscribe(consumer, topics);
    while (true) {
        rd_kafka_message_t* msg = rd_kafka_consumer_poll(consumer, 100);
        if (msg) {
            std::string response = std::string((char*)msg->payload, msg->len);

            if (response.size() > 0) {
                // ƒобавл€ем ответ в очередь
                {
                    std::lock_guard<std::mutex> lock(queueMutex);
                    //if (counter > 12 && !responseQueue.empty()) {
                    //    while (!responseQueue.empty())
                    //        responseQueue.pop();
                    //    counter = 0;
                    //}
                    //else {
                     //   if (responseQueue.empty())
                    //        counter = 0;
                   // }
                    responseQueue.push(response);
                }

                // ”ведомл€ем главный поток, что ответ пришел
                responseCondition.notify_one();
            }

            // ”ведомл€ем главный поток, что ответ пришел
            responseCondition.notify_one();

            rd_kafka_message_destroy(msg);
        }
    }

    rd_kafka_consumer_close(consumer);
    rd_kafka_destroy(consumer);
}

std::string ConsumerPublisher::awaitResponseFromOutTopic()
{
    std::unique_lock<std::mutex> lock(queueMutex);

    // ќжидаем, пока в очереди не по€витс€ ответ
    responseCondition.wait(lock, [] { return !responseQueue.empty(); });

    // ѕолучаем первый ответ из очереди
    std::string response = responseQueue.front();
    responseQueue.pop();
    return response;
}
