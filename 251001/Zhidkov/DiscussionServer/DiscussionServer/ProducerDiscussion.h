#pragma once
#include <string>
class ProducerDiscussion
{
public:
	static void sendToKafka(const std::string& topic, const std::string& message);
};

