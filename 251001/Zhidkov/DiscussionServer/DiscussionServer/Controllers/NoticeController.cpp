#include <nlohmann/json.hpp>
#include "NoticeController.h"
#include "../ProducerDiscussion.h"
#include "../Services/NoticeService.h"
bool putIsDone = false;
using json = nlohmann::json;
typename json responseToJson(const crow::response& resp) {
    json j;

    // Код ответа (всегда присутствует)
    j["code"] = resp.code;

    j["body"] = resp.body;

    // Заголовки (могут отсутствовать)
    if (!resp.headers.empty()) {
        json headers;
        for (const auto& header : resp.headers) {
            headers[header.first] = header.second;
        }
        j["headers"] = headers;
    }

    // Флаги
    j["manual_length_header"] = resp.manual_length_header;
    j["skip_body"] = resp.skip_body;

    return j;
}

crow::response jsonToResponse(const json& j) {
    // Извлекаем код ответа
    int code = j.contains("code") ? j["code"].get<int>() : 200;

    // Извлекаем тело ответа (если есть)
    std::string body = j.contains("body") ? j["body"].get<std::string>() : "";

    // Создаём объект ответа
    crow::response resp(code, body);

    // Восстанавливаем заголовки (если есть)
    if (j.contains("headers") && j["headers"].is_object()) {
        for (const auto& [key, value] : j["headers"].items()) {
            resp.add_header(key, value.get<std::string>());
        }
    }

    // Восстанавливаем флаги (если есть)
    resp.manual_length_header = j.contains("manual_length_header") ? j["manual_length_header"].get<bool>() : false;
    resp.skip_body = j.contains("skip_body") ? j["skip_body"].get<bool>() : false;

    return resp;
}


crow::response NoticeController::get_notices(const crow::request& req)
{
    std::vector<Notice> notices = NoticeService::get_all();
    //crow::response resp = NoticeController::get_notices(req);

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
        //std::string str = responseToJson(crow::response(result)).dump();
        ProducerDiscussion::sendToKafka("OutTopic", "{}");
        return crow::response(result);
    }
    //std::cout << result.dump(4) << std::endl;
    std::cout << result1.dump(4) << std::endl;
    //std::cout << users_list.front().dump(4) << std::endl;
    std::cout << "GETALL" << std::endl;
    std::string str = responseToJson(crow::response(result1)).dump();
    ProducerDiscussion::sendToKafka("OutTopic", str);
    return crow::response(result1);
}

crow::response NoticeController::get_notice(const crow::request& req, int id)
{
    std::cout << "GET" << std::endl;
    //bool success = read_user_from_db(id);

    auto notice = NoticeService::get_by_id(id);

    if (notice) {
        crow::json::wvalue res_json;
        if (putIsDone) {
            int yyy = 10101;
            res_json["hash"] = yyy;
            putIsDone = false;
        }
        // Если пользователь найден, возвращаем его в формате JSON
        
        res_json["id"] = notice->id;
        res_json["newsId"] = notice->newsId;
        res_json["content"] = notice->content;
        
        std::string str = responseToJson(crow::response(200, res_json)).dump();
        ProducerDiscussion::sendToKafka("OutTopic", str);
        return crow::response(200, res_json);
    }
    else {
        std::string str = responseToJson(crow::response(404, "{}")).dump();
        ProducerDiscussion::sendToKafka("OutTopic", str);
        // Если пользователь не найден, возвращаем ошибку 404
        return crow::response(404, "{ }");
    }
}

crow::response NoticeController::create_notice(const crow::request& req)
{
    std::cout << "CREATE" << std::endl;
    CROW_LOG_INFO << "Received body: " << req.body;  // Логируем тело запроса

    auto json_data = json::parse(req.body);
    if (json_data.empty()) {
        std::string str = responseToJson(crow::response(400, R"({"error": "Invalid JSON"})")).dump();
        ProducerDiscussion::sendToKafka("OutTopic", str);
        return crow::response(400, R"({"error": "Invalid JSON"})");
    }

    if (json_data.empty() || !json_data.contains("content") || !json_data.contains("newsId")) {
        std::string str = responseToJson(crow::response(400, R"({"error": "Invalid JSON"})")).dump();
        ProducerDiscussion::sendToKafka("OutTopic", str);
        return crow::response(400, R"({"error": "Missing required fields"})");
    }

    Notice notice;
    //new_user.id = json_data["id"];
    notice.newsId = (int)json_data["newsId"];
    notice.content = json_data["content"];

    if (NoticeService::create(notice)) {
        std::optional<int> id = NoticeService::get_id(notice);
        if (!id.has_value()) {
            std::string str = responseToJson(crow::response(400, R"({"error": "Failed to retrieve user ID"})")).dump();
            ProducerDiscussion::sendToKafka("OutTopic", str);
            std::cout << "No value" << std::endl;
            return crow::response(400, R"({"error": "Failed to retrieve user ID"})");
        }

        crow::json::wvalue res_json;
        res_json["id"] = static_cast<int>(id.value());
        res_json["newsId"] = notice.newsId;
        res_json["content"] = notice.content;
        std::string str = responseToJson(crow::response(201, res_json)).dump();
        ProducerDiscussion::sendToKafka("OutTopic", str);
        return crow::response(201, res_json);
    }
    else {
        std::string str = responseToJson(crow::response(400, R"({"error": "Database error"})")).dump();
        ProducerDiscussion::sendToKafka("OutTopic", str);
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
            std::string str = responseToJson(crow::response(204)).dump();
            ProducerDiscussion::sendToKafka("OutTopic", str);
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
        std::string str = responseToJson(crow::response(404, res_json)).dump();
        ProducerDiscussion::sendToKafka("OutTopic", str);
        return crow::response(404, res_json);
    }
}

crow::response NoticeController::update_notice(const crow::request& req)
{
    putIsDone = true;
    crow::json::wvalue res_json;
    std::cout << "PUT" << std::endl;
    // Загружаем данные из тела запроса
    auto json_data = json::parse(req.body);

    if (json_data.empty() || !json_data.contains("newsId") || !json_data.contains("id") || !json_data.contains("content")) {
        std::string str = responseToJson(crow::response(400, "Invalid JSON")).dump();
        ProducerDiscussion::sendToKafka("OutTopic", str);
        return crow::response(400, "Invalid JSON");
    }

    // Извлекаем данные из JSON
    int id = (int)json_data["id"];
    int newsId = (int)json_data["newsId"];
    std::string content = json_data["content"];
    //if (user_exists(id)) {
        // Обновление пользователя в базе данных
    bool success = NoticeService::update(id, newsId, content);

    if (success) {
        auto notice = NoticeService::get_by_id(id);

        if (notice) {
            // Если пользователь найден, возвращаем его в формате JSON
            /*if (notice->content != content && notice->newsId != newsId) {
                res_json["status"] = "error";
                res_json["message"] = "Notice not updated";
                std::string str = responseToJson(crow::response(400, res_json)).dump();
                ProducerDiscussion::sendToKafka("OutTopic", str);
                return crow::response(400, res_json);
            }*/
            res_json["id"] = notice->id;
            res_json["newsId"] = notice->newsId;
            res_json["content"] = notice->content;
            std::string str = responseToJson(crow::response(200, res_json)).dump();
            ProducerDiscussion::sendToKafka("OutTopic", str);
            return crow::response(200, res_json);
        }
        else {
            res_json["status"] = "error";
            res_json["message"] = "Notice not found";
            std::string str = responseToJson(crow::response(404, res_json)).dump();
            ProducerDiscussion::sendToKafka("OutTopic", str);
            return crow::response(404, res_json);
        }
    }
    else {
        res_json["status"] = "error";
        res_json["message"] = "Notice not found";
        std::string str = responseToJson(crow::response(404, res_json)).dump();
        ProducerDiscussion::sendToKafka("OutTopic", str);
        return crow::response(404, res_json);
    }
};