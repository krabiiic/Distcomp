#include "UserController.h"
#include "../Services/UserService.h"
crow::response UserController::get_users(const crow::request& req) {
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

    return crow::response(result1);
}
crow::response UserController::get_user(const crow::request& req, int id) {
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