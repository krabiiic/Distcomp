#pragma once
#include <queue>
#include <atomic>
#include <mutex>
#include <condition_variable>
extern std::atomic<int> counter;
class ConsumerPublisher
{
public:
	static std::queue<std::string> responseQueue;
	static std::mutex queueMutex;
	static std::condition_variable responseCondition;
public:
	static void consumeOutTopic();
	static std::string awaitResponseFromOutTopic();
};

