#pragma once
#include <crow.h>
class LabelController {
public:
    static crow::response get_labels(const crow::request& req);

    static crow::response get_label(const crow::request& req, int id);

    static crow::response create_label(const crow::request& req);

    static crow::response delete_label(const crow::request& req, int id);

    static crow::response update_label(const crow::request& req);
};