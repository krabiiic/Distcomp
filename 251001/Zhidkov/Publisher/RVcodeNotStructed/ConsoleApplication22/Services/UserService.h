#pragma once
#include <string>
#include <vector>
#include <optional>
#include "../Models/User.h"
class UserService {
public:
    static std::optional<User> get_by_id(int id);
    static bool exists(int user_id);
    static std::vector<User> get_all();
    static std::optional<int> get_id(const User& user);
    static bool create(const User& entity);
    static bool update(int id, const std::string& login, const std::string& password,
        const std::string& firstname, const std::string& lastname);
    static bool delete_by_id(int id);
};