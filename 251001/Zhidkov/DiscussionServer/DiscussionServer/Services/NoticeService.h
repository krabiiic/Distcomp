#pragma once
#include <string>
#include <vector>
#include <optional>
#include "../Models/Notice.h"
class NoticeService {
public:
    static std::optional<Notice> get_by_id(int id);
    static bool exists(int id);
    static std::vector<Notice> get_all();
    static std::optional<int> get_id(const Notice& notice);
    static bool create(const Notice& notice);
    static bool update(int id, int newsId, const std::string& content);
    static bool delete_by_id(int id);
};