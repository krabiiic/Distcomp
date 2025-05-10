#include "NoticeController.h"
#include "../Services/NoticeService.h"
crow::response NoticeController::get_notices(const crow::request& req)
{
    std::vector<Notice> notices = NoticeService::get_all();

    // Формируем JSON массив
    crow::json::wvalue result;
    crow::json::wvalue result1;
    crow::json::wvalue::list notices_list;  // Вектор JSON-объектов

    for (const auto& notice : notices) {
        notices_list.push_back(crow::json::wvalue{
            {"id", notice.id},
            {"newsId", notice.newsId},
            {"content", notice.content},
            });
        std::cout << notice.id << std::endl;
        std::cout << notice.newsId << std::endl;
    }

    //result["users"] = std::move(users_list);  // Записываем массив в JSON-ответ
    if (!notices_list.empty()) {
        result1 = std::move(notices_list.front());
        //result1 = std::move(news_list.front());
    }
    else {
        result["notices"] = std::move(notices_list);
        return crow::response(result);
    }
    //std::cout << result.dump(4) << std::endl;
    std::cout << result1.dump(4) << std::endl;
    //std::cout << users_list.front().dump(4) << std::endl;
    std::cout << "GETALL" << std::endl;

    return crow::response(result1);
}

crow::response NoticeController::get_notice(const crow::request& req, int id)
{
    std::cout << "READ GET" << std::endl;
    //bool success = read_user_from_db(id);

    auto notice = NoticeService::get_by_id(id);

    if (notice) {
        // Если пользователь найден, возвращаем его в формате JSON
        crow::json::wvalue res_json;
        res_json["id"] = notice->id;
        res_json["newsId"] = notice->newsId;
        res_json["content"] = notice->content;

        return crow::response(200, res_json);
    }
    else {
        // Если пользователь не найден, возвращаем ошибку 404
        return crow::response(404, "Notice not found");
    }
}

crow::response NoticeController::create_notice(const crow::request& req)
{
    CROW_LOG_INFO << "Received body: " << req.body;  // Логируем тело запроса

    auto json_data = crow::json::load(req.body);
    if (!json_data) {
        return crow::response(400, R"({"error": "Invalid JSON"})");
    }

    if (!json_data || !json_data.has("content") || !json_data.has("newsId")) {
        return crow::response(400, R"({"error": "Missing required fields"})");
    }

    Notice notice;
    //new_user.id = json_data["id"];
    notice.newsId = json_data["newsId"].i();
    notice.content = json_data["content"].s();

    if (NoticeService::create(notice)) {
        std::optional<int> id = NoticeService::get_id(notice);
        if (!id.has_value()) {
            std::cout << "No value" << std::endl;
            return crow::response(400, R"({"error": "Failed to retrieve user ID"})");
        }

        crow::json::wvalue res_json;
        res_json["id"] = static_cast<int>(id.value());
        res_json["newsId"] = notice.newsId;
        res_json["content"] = notice.content;
        return crow::response(201, res_json);
    }
    else {
        std::cout << "db error" << std::endl;
        return crow::response(400, R"({"error": "Database error"})");
    }
}

crow::response NoticeController::delete_notice(const crow::request& req, int id)
{
    crow::json::wvalue res_json;
    std::cout << "DELETE" << std::endl;
    if (NoticeService::exists(id)) {
        bool success = NoticeService::delete_by_id(id);

        if (success) {
            res_json["status"] = "success";
            res_json["message"] = "Notice deleted successfully";
            return crow::response(204);
        }
        else {
            res_json["status"] = "error";
            res_json["message"] = "Failed to delete news";
            return crow::response(500, res_json);
        }
    }
    else {
        res_json["status"] = "error";
        res_json["message"] = "Notice not found";
        return crow::response(404, res_json);
    }
}

crow::response NoticeController::update_notice(const crow::request& req)
{
    crow::json::wvalue res_json;
    std::cout << "PUT" << std::endl;
    // Загружаем данные из тела запроса
    auto json_data = crow::json::load(req.body);

    if (!json_data || !json_data.has("newsId") || !json_data.has("id") || !json_data.has("content")) {
        return crow::response(400, "Invalid JSON");
    }

    // Извлекаем данные из JSON
    int id = json_data["id"].i();
    int newsId = json_data["newsId"].i();
    std::string content = json_data["content"].s();
    //if (user_exists(id)) {
        // Обновление пользователя в базе данных
    bool success = NoticeService::update(id, newsId, content);

    if (success) {
        auto notice = NoticeService::get_by_id(id);

        if (notice) {
            // Если пользователь найден, возвращаем его в формате JSON

            res_json["id"] = notice->id;
            res_json["newsId"] = notice->newsId;
            res_json["content"] = notice->content;

            return crow::response(200, res_json);
        }
        else {
            res_json["status"] = "error";
            res_json["message"] = "Notice not found";
            return crow::response(404, res_json);
        }
    }
    else {
        res_json["status"] = "error";
        res_json["message"] = "Notice not found";
        return crow::response(404, res_json);
    }
}
