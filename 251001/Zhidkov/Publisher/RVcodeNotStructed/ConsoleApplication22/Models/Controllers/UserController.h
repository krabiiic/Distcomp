#pragma once
#include <crow.h>
class UserController {
public:
    static crow::response get_users(const crow::request& req);

    static crow::response get_user(const crow::request& req, int id);

    static crow::response create_user(const crow::request& req);

    static crow::response delete_user(const crow::request& req, int id);

    static crow::response update_user(const crow::request& req);
};