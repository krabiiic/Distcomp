#include <hiredis/hiredis.h>
#include "LabelController.h"
#include "../Services/LabelService.h"

extern redisContext* redis_context;
std::string generate_cache_key_label(const crow::request& req) {
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
void delete_related_keys_label(const std::string& str) {
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
crow::response LabelController::get_labels(const crow::request& req)
{
    // Генерируем ключ для кэша
    std::string cache_key = generate_cache_key_label(req);

    // Проверяем, есть ли кэшированный ответ
    redisReply* reply = (redisReply*)redisCommand(redis_context, "GET %s", cache_key.c_str());

    if (reply->type == REDIS_REPLY_STRING) {
        // Если кэш найден, возвращаем его
        //std::cout << "Cache hit!" << std::endl;
        return crow::response(reply->str);  // Возвращаем кэшированный ответ
    }
    std::vector<Label> labels = LabelService::get_all();

    // Формируем JSON массив
    crow::json::wvalue result;
    crow::json::wvalue result1;
    crow::json::wvalue::list labels_list;  // Вектор JSON-объектов

    for (const auto& label : labels) {
        labels_list.push_back(crow::json::wvalue{
            {"id", label.id},
            {"name", label.name},
            });
    }

    //result["users"] = std::move(users_list);  // Записываем массив в JSON-ответ
    if (!labels_list.empty()) {
        result1 = std::move(labels_list.front());
        //result1 = std::move(news_list.front());
    }
    else {
        result["labels"] = std::move(labels_list);
        return crow::response(result);
    }
    //std::cout << result.dump(4) << std::endl;
    std::cout << result1.dump(4) << std::endl;
    //std::cout << users_list.front().dump(4) << std::endl;
    std::cout << "GETALL" << std::endl;
    // Сохраняем результат в кэш
    redisCommand(redis_context, "SET %s %s", cache_key.c_str(), result1.dump().c_str());
    return crow::response(result1);
}

crow::response LabelController::get_label(const crow::request& req, int id)
{
    // Генерация ключа для GET-запроса по ID пользователя
    std::string cache_key = generate_cache_key_label(req);

    // Проверка наличия кэша
    redisReply* reply = (redisReply*)redisCommand(redis_context, "GET %s", cache_key.c_str());

    if (reply->type == REDIS_REPLY_STRING) {
        // Если кэш найден, возвращаем его
        //std::cout << "Cache hit!" << std::endl;
        return crow::response(reply->str);  // Возвращаем кэшированный ответ
    }
    std::cout << "READ GET" << std::endl;
    //bool success = read_user_from_db(id);

    auto label = LabelService::get_by_id(id);

    if (label) {
        // Если пользователь найден, возвращаем его в формате JSON
        crow::json::wvalue res_json;
        res_json["id"] = label->id;
        res_json["name"] = label->name;

        // Кэшируем результат
        redisCommand(redis_context, "SET %s %s", cache_key.c_str(), res_json.dump().c_str());
        return crow::response(200, res_json);
    }
    else {
        // Если пользователь не найден, возвращаем ошибку 404
        return crow::response(404, "Notice not found");
    }
}

crow::response LabelController::create_label(const crow::request& req)
{
    CROW_LOG_INFO << "Received body: " << req.body;  // Логируем тело запроса

    auto json_data = crow::json::load(req.body);
    if (!json_data) {
        return crow::response(400, R"({"error": "Invalid JSON"})");
    }

    if (!json_data || !json_data.has("name")) {
        return crow::response(400, R"({"error": "Missing required fields"})");
    }

    Label label;
    //new_user.id = json_data["id"];
    label.name = json_data["name"].s();

    if (LabelService::create(label)) {
        std::optional<int> id = LabelService::get_id(label);
        if (!id.has_value()) {
            std::cout << "No value" << std::endl;
            return crow::response(400, R"({"error": "Failed to retrieve user ID"})");
        }

        crow::json::wvalue res_json;
        res_json["id"] = static_cast<int>(id.value());
        res_json["name"] = label.name;
        // Удаляем кэшированные данные для этого пользователя
        //redisCommand(redis_context, "DEL %s", generate_cache_key(req).c_str());

        //// Очистим кэш для списка пользователей
        delete_related_keys_label("labels");
        return crow::response(201, res_json);
    }
    else {
        std::cout << "db error" << std::endl;
        return crow::response(400, R"({"error": "Database error"})");
    }
}

crow::response LabelController::delete_label(const crow::request& req, int id)
{
    crow::json::wvalue res_json;
    std::cout << "DELETE" << std::endl;
    if (LabelService::exists(id)) {
        bool success = LabelService::delete_by_id(id);

        if (success) {
            res_json["status"] = "success";
            res_json["message"] = "Notice deleted successfully";
            // Удаляем кэшированные данные для этого пользователя
            //redisCommand(redis_context, "DEL %s", generate_cache_key(req).c_str());

            //// Очистим кэш для списка пользователей
            delete_related_keys_label("labels");
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

crow::response LabelController::update_label(const crow::request& req)
{
    crow::json::wvalue res_json;
    std::cout << "PUT" << std::endl;
    // Загружаем данные из тела запроса
    auto json_data = crow::json::load(req.body);

    if (!json_data || !json_data.has("name") || !json_data.has("id")) {
        return crow::response(400, "Invalid JSON");
    }

    // Извлекаем данные из JSON
    int id = json_data["id"].i();
    std::string name = json_data["name"].s();
    //if (user_exists(id)) {
        // Обновление пользователя в базе данных
    bool success = LabelService::update(id, name);

    if (success) {
        // Очистить кэш для этого пользователя
        //redisCommand(redis_context, "DEL %s", generate_cache_key(req).c_str());

        //// Очистим кэш для списка пользователей
        delete_related_keys_label("labels");
        auto label = LabelService::get_by_id(id);

        if (label) {
            // Если пользователь найден, возвращаем его в формате JSON

            res_json["id"] = label->id;
            res_json["name"] = label->name;

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
