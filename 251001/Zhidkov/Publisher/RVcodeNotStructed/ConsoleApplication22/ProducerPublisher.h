#pragma once
#include <string>
class ProducerPublisher
{
public:
    static void sendToKafka(const std::string& topic, const std::string& message);
};

