#pragma once
#include <queue>
#include <mutex>
#include <condition_variable>
class ConsumerDiscussion
{
public:
	static std::queue<std::string> responseQueue;
	static std::mutex queueMutex;
	static std::condition_variable responseCondition;
public:
	static void consumeInTopic();
	static void awaitResponseFromInTopic();
};
