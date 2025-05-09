#pragma once
#include <atomic>
#include <crow.h>
extern std::atomic<int> counter;
class NoticeController {
public:
    static crow::response get_notices(const crow::request& req);

    static crow::response get_notice(const crow::request& req, int id);

    static crow::response create_notice(const crow::request& req);

    static crow::response delete_notice(const crow::request& req, int id);

    static crow::response update_notice(const crow::request& req);
};
