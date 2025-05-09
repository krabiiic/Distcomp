#include <hiredis/hiredis.h>
#include "NewsController.h"
#include "../Services/NewsService.h"

extern redisContext* redis_context;
std::string generate_cache_key_news(const crow::request& req) {
    // Генерация ключа для кэша на основе метода и тела запроса
    std::string method_str;

    switch (req.method) {
    case crow::HTTPMethod::GET:    method_str = "GET"; break;
    case crow::HTTPMethod::POST:   method_str = "POST"; break;
    case crow::HTTPMethod::PUT:    method_str = "PUT"; break;
    case crow::HTTPMethod::Delete: method_str = "DELETE"; break;
        // Можно добавить другие методы, если нужно
    default: method_str = "UNKNOWN"; break;
    }

    // Генерация ключа
    return method_str + ":" + req.url + ":" + req.body;
}
void delete_related_keys_news(const std::string& str) {
    //redisCommand(redis_context, "FLUSHALL");
    redisReply* reply = (redisReply*)redisCommand(redis_context, std::string("SCAN 0 MATCH *" + str + "*").c_str());

    if (reply->type == REDIS_REPLY_ARRAY) {
        for (size_t i = 0; i < reply->elements; ++i) {
            if (reply->element[i]->str && reply->element[i]->str != "0") {
                std::string key = reply->element[i]->str;
                redisCommand(redis_context, "DEL %s", key.c_str());
            }
            else {
                if (!reply->element[i]->str) {
                    for (size_t j = 0; j < reply->element[i]->elements; ++j) {
                        if (reply->element[i]->element[j]->str && reply->element[i]->element[j]->str != "0") {
                            std::string key = reply->element[i]->element[j]->str;
                            redisCommand(redis_context, "DEL %s", key.c_str());
                        }
                    }
                }
            }
        }
    }
}

crow::response NewsController::get_newss(const crow::request& req)
{
    // Генерируем ключ для кэша
    std::string cache_key = generate_cache_key_news(req);

    // Проверяем, есть ли кэшированный ответ
    redisReply* reply = (redisReply*)redisCommand(redis_context, "GET %s", cache_key.c_str());

    if (reply->type == REDIS_REPLY_STRING) {
        // Если кэш найден, возвращаем его
        //std::cout << "Cache hit!" << std::endl;
        return crow::response(reply->str);  // Возвращаем кэшированный ответ
    }
    std::vector<News> newss = NewsService::get_all();

    // Формируем JSON массив
    crow::json::wvalue result;
    crow::json::wvalue result1;
    crow::json::wvalue::list news_list;  // Вектор JSON-объектов

    for (const auto& news : newss) {
        news_list.push_back(crow::json::wvalue{
            {"id", news.id},
            {"userId", news.userId},
            {"title", news.title},
            {"content", news.content},
            });
        std::cout << news.id << std::endl;
        std::cout << news.userId << std::endl;
    }

    //result["users"] = std::move(users_list);  // Записываем массив в JSON-ответ
    if (!news_list.empty()) {
        result1 = std::move(news_list.front());
        //result1 = std::move(news_list.front());
    }
    else {
        result["newss"] = std::move(news_list);
        return crow::response(result);
    }
    //std::cout << result.dump(4) << std::endl;
    std::cout << result1.dump(4) << std::endl;
    //std::cout << users_list.front().dump(4) << std::endl;
    std::cout << "GETALL" << std::endl;

    redisCommand(redis_context, "SET %s %s", cache_key.c_str(), result1.dump().c_str());
    return crow::response(result1);
}

crow::response NewsController::get_news(const crow::request& req, int id)
{
    // Генерация ключа для GET-запроса по ID пользователя
    std::string cache_key = generate_cache_key_news(req);

    // Проверка наличия кэша
    redisReply* reply = (redisReply*)redisCommand(redis_context, "GET %s", cache_key.c_str());

    if (reply->type == REDIS_REPLY_STRING) {
        // Если кэш найден, возвращаем его
        //std::cout << "Cache hit!" << std::endl;
        return crow::response(reply->str);  // Возвращаем кэшированный ответ
    }
    std::cout << "READ GET" << std::endl;
    //bool success = read_user_from_db(id);

    auto news = NewsService::get_by_id(id);

    if (news) {
        // Если пользователь найден, возвращаем его в формате JSON
        crow::json::wvalue res_json;
        res_json["id"] = news->id;
        res_json["userId"] = news->userId;
        res_json["title"] = news->title;
        res_json["content"] = news->content;

        // Кэшируем результат
        redisCommand(redis_context, "SET %s %s", cache_key.c_str(), res_json.dump().c_str());
        return crow::response(200, res_json);
    }
    else {
        // Если пользователь не найден, возвращаем ошибку 404
        return crow::response(404, "User not found");
    }
}

crow::response NewsController::create_news(const crow::request& req)
{
    CROW_LOG_INFO << "Received body: " << req.body;  // Логируем тело запроса

    auto json_data = crow::json::load(req.body);
    if (!json_data) {
        return crow::response(403, R"({"error": "Invalid JSON"})");
    }

    if (!json_data.has("title") || !json_data.has("content") || !json_data.has("userId")) {
        return crow::response(403, R"({"error": "Missing required fields"})");
    }

    News news;

    if (json_data["userId"].t() == crow::json::type::Number) {
        news.userId = json_data["userId"].i();
    }
    else {
        return crow::response(403, R"({"error": "UserId must be a number"})");
    }

    if (json_data["title"].t() == crow::json::type::String) {
        news.title = json_data["title"].s();
    }
    else {
        return crow::response(403, R"({"error": "Title must be a string"})");
    }

    if (json_data["content"].t() == crow::json::type::String) {
        news.content = json_data["content"].s();
    }
    else {
        return crow::response(403, R"({"error": "Content must be a string"})");
    }

    //news.created = json_data["created"].s();
    //news.modified = json_data["modified"].s();
    //if (news_exists())
    if (NewsService::create(news)) {
        std::optional<int> id = NewsService::get_id(news);
        if (!id.has_value()) {
            std::cout << "No value" << std::endl;
            return crow::response(403, R"({"error": "Failed to retrieve user ID"})");
        }

        crow::json::wvalue res_json;
        res_json["id"] = static_cast<int>(id.value());
        res_json["userId"] = news.userId;
        res_json["title"] = news.title;
        res_json["content"] = news.content;
        //res_json["created"] = news.created;
        //res_json["modified"] = news.modified;

        // Удаляем кэшированные данные для этого пользователя
        //redisCommand(redis_context, "DEL %s", generate_cache_key(req).c_str());

        //// Очистим кэш для списка пользователей
        delete_related_keys_news("news");
        return crow::response(201, res_json);
    }
    else {
        std::cout << "db error" << std::endl;
        return crow::response(403, R"({"error": "Database error"})");
    }
}

crow::response NewsController::delete_news(const crow::request& req, int id)
{
    crow::json::wvalue res_json;
    std::cout << "DELETE" << std::endl;
    if (NewsService::exists(id)) {
        bool success = NewsService::delete_by_id(id);

        if (success) {
            res_json["status"] = "success";
            res_json["message"] = "News deleted successfully";

            // Удаляем кэшированные данные для этого пользователя
            //redisCommand(redis_context, "DEL %s", generate_cache_key(req).c_str());

            //// Очистим кэш для списка пользователей
            delete_related_keys_news("news");
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
        res_json["message"] = "News not found";
        return crow::response(404, res_json);
    }
}

crow::response NewsController::update_news(const crow::request& req)
{
    crow::json::wvalue res_json;
    std::cout << "PUT" << std::endl;
    // Загружаем данные из тела запроса
    auto json_data = crow::json::load(req.body);

    if (!json_data || !json_data.has("userId") || !json_data.has("id") ||
        !json_data.has("title") || !json_data.has("content")) {
        return crow::response(400, "Invalid JSON");
    }

    // Извлекаем данные из JSON
    int id = json_data["id"].i();
    int userId = json_data["userId"].i();
    std::string title = json_data["title"].s();
    std::string content = json_data["content"].s();
    //if (user_exists(id)) {
        // Обновление пользователя в базе данных
    bool success = NewsService::update(id, userId, title, content);

    if (success) {
        // Очистить кэш для этого пользователя
        //redisCommand(redis_context, "DEL %s", generate_cache_key(req).c_str());

        //// Очистим кэш для списка пользователей
        delete_related_keys_news("news");
        auto news = NewsService::get_by_id(id);

        if (news) {
            // Если пользователь найден, возвращаем его в формате JSON

            res_json["id"] = news->id;
            res_json["userId"] = news->userId;
            res_json["title"] = news->title;
            res_json["content"] = news->content;

            return crow::response(200, res_json);
        }
        else {
            res_json["status"] = "error";
            res_json["message"] = "User not found";
            return crow::response(404, res_json);
        }
    }
    else {
        res_json["status"] = "error";
        res_json["message"] = "User not found";
        return crow::response(404, res_json);
    }
}
