#pragma once
#include <string>
#include <vector>
#include <optional>
#include "../Models/News.h"
class NewsService {
public:
    static std::optional<News> get_by_id(int id);
    static bool exists(int id);
    static std::vector<News> get_all();
    static std::optional<int> get_id(const News& news);
    static bool create(const News& news);
    static bool update(int id, int userId, const std::string& title, const std::string& content);
    static bool delete_by_id(int id);
};