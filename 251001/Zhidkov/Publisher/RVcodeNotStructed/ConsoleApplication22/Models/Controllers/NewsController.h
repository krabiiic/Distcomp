#pragma once
#include <crow.h>
class NewsController {
public:
    static crow::response get_newss(const crow::request& req);

    static crow::response get_news(const crow::request& req, int id);

    static crow::response create_news(const crow::request& req);

    static crow::response delete_news(const crow::request& req, int id);

    static crow::response update_news(const crow::request& req);
};