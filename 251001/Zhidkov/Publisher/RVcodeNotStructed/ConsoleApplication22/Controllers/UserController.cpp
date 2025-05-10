#include <hiredis/hiredis.h>
#include "UserController.h"
#include "../Services/UserService.h"

extern redisContext* redis_context;
std::string generate_cache_key_user(const crow::request& req) {
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
void delete_related_keys_user(const std::string &str) {
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

crow::response UserController::get_users(const crow::request& req) {
    // Генерируем ключ для кэша
    std::string cache_key = generate_cache_key_user(req);

    // Проверяем, есть ли кэшированный ответ
    redisReply* reply = (redisReply*)redisCommand(redis_context, "GET %s", cache_key.c_str());

    if (reply->type == REDIS_REPLY_STRING) {
        // Если кэш найден, возвращаем его
        //std::cout << "Cache hit!" << std::endl;
        return crow::response(reply->str);  // Возвращаем кэшированный ответ
    }

    // Если кэш не найден, выполняем запрос к базе данных
    std::vector<User> users = UserService::get_all();

    // Формируем JSON массив
    crow::json::wvalue result;
    crow::json::wvalue result1;
    crow::json::wvalue::list users_list;  // Вектор JSON-объектов

    for (const auto& user : users) {
        users_list.push_back(crow::json::wvalue{
            {"id", user.id},
            {"login", user.login},
            {"password", user.password},
            {"firstname", user.firstname},
            {"lastname", user.lastname}
            });
    }

    //result["users"] = std::move(users_list);  // Записываем массив в JSON-ответ
    if (!users_list.empty()) {
        result1 = std::move(users_list.front());
    }
    else {
        result["users"] = std::move(users_list);
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

crow::response UserController::get_user(const crow::request& req, int id) {
    // Генерация ключа для GET-запроса по ID пользователя
    std::string cache_key = generate_cache_key_user(req);

    // Проверка наличия кэша
    redisReply* reply = (redisReply*)redisCommand(redis_context, "GET %s", cache_key.c_str());

    if (reply->type == REDIS_REPLY_STRING) {
        // Если кэш найден, возвращаем его
        //std::cout << "Cache hit!" << std::endl;
        return crow::response(reply->str);  // Возвращаем кэшированный ответ
    }

    std::cout << "READ GET" << std::endl;
    //bool success = read_user_from_db(id);

    auto user = UserService::get_by_id(id);

    if (user) {
        // Если пользователь найден, возвращаем его в формате JSON
        crow::json::wvalue res_json;
        res_json["id"] = user->id;
        res_json["login"] = user->login;
        res_json["password"] = user->password;
        res_json["firstname"] = user->firstname;
        res_json["lastname"] = user->lastname;

        // Кэшируем результат
        redisCommand(redis_context, "SET %s %s", cache_key.c_str(), res_json.dump().c_str());

        return crow::response(200, res_json);
    }
    else {
        // Если пользователь не найден, возвращаем ошибку 404
        return crow::response(404, "User not found");
    }
}

crow::response UserController::create_user(const crow::request& req) {
    CROW_LOG_INFO << "Received body: " << req.body;  // Логируем тело запроса

    auto json_data = crow::json::load(req.body);
    if (!json_data) {
        return crow::response(403, R"({"error": "Invalid JSON"})");
    }

    if (!json_data.has("login") || !json_data.has("password") ||
        !json_data.has("firstname") || !json_data.has("lastname")) {
        return crow::response(403, R"({"error": "Missing required fields"})");
    }

    User new_user;
    //new_user.id = json_data["id"];
    new_user.login = json_data["login"].s();
    new_user.password = json_data["password"].s();
    new_user.firstname = json_data["firstname"].s();
    new_user.lastname = json_data["lastname"].s();

    if (UserService::create(new_user)) {
        std::optional<int> user_id = UserService::get_id(new_user);
        if (!user_id.has_value()) {
            std::cout << "No value" << std::endl;
            return crow::response(403, R"({"error": "Failed to retrieve user ID"})");
        }

        crow::json::wvalue res_json;
        res_json["id"] = static_cast<int>(user_id.value());
        res_json["login"] = new_user.login;
        res_json["password"] = new_user.password;
        res_json["firstname"] = new_user.firstname;
        res_json["lastname"] = new_user.lastname;
        // Удаляем кэшированные данные для этого пользователя
        //redisCommand(redis_context, "DEL %s", generate_cache_key(req).c_str());

        //// Очистим кэш для списка пользователей
        delete_related_keys_user("users");
        return crow::response(201, res_json);
    }
    else {
        std::cout << "db error" << std::endl;
        return crow::response(403, R"({"error": "Database error"})");
    }
}
crow::response UserController::delete_user(const crow::request& req, int id) {
    crow::json::wvalue res_json;
    std::cout << "DELETE" << std::endl;
    if (UserService::exists(id)) {
        bool success = UserService::delete_by_id(id);

        if (success) {
            // Удаляем кэшированные данные для этого пользователя
            //redisCommand(redis_context, "DEL %s", generate_cache_key(req).c_str());

            //// Очистим кэш для списка пользователей
            delete_related_keys_user("users");
            res_json["status"] = "success";
            res_json["message"] = "User deleted successfully";
            return crow::response(204);
        }
        else {
            res_json["status"] = "error";
            res_json["message"] = "Failed to delete user";
            return crow::response(500, res_json);
        }
    }
    else {
        res_json["status"] = "error";
        res_json["message"] = "User not found";
        return crow::response(404, res_json);
    }
}

crow::response UserController::update_user(const crow::request& req) {
    crow::json::wvalue res_json;
    std::cout << "PUT" << std::endl;
    // Загружаем данные из тела запроса
    auto json_data = crow::json::load(req.body);

    if (!json_data || !json_data.has("login") || !json_data.has("id") || !json_data.has("password") ||
        !json_data.has("firstname") || !json_data.has("lastname")) {
        return crow::response(400, "Invalid JSON");
    }

    // Извлекаем данные из JSON
    int id = json_data["id"].i();
    std::string login = json_data["login"].s();
    std::string password = json_data["password"].s();
    std::string firstname = json_data["firstname"].s();
    std::string lastname = json_data["lastname"].s();
    //if (user_exists(id)) {
        // Обновление пользователя в базе данных
    bool success = UserService::update(id, login, password, firstname, lastname);

    if (success) {
        // Очистить кэш для этого пользователя
        //redisCommand(redis_context, "DEL %s", generate_cache_key(req).c_str());

        //// Очистим кэш для списка пользователей
        delete_related_keys_user("users");
        auto user = UserService::get_by_id(id);

        if (user) {
            // Если пользователь найден, возвращаем его в формате JSON

            res_json["id"] = user->id;
            res_json["login"] = user->login;
            res_json["password"] = user->password;
            res_json["firstname"] = user->firstname;
            res_json["lastname"] = user->lastname;

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
