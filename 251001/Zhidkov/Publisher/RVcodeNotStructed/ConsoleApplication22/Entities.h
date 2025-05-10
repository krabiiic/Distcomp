#pragma once
#include <json/json.h> 
//#include "User.h"
//class User;
template<typename T>
class Entity {
public:
    virtual T get_by_id(int id) = 0;
    virtual std::vector<T> get_all() = 0;
    virtual void create(const T& entity) = 0;
    virtual void update(int id, const T& entity) = 0;
    virtual void delete_by_id(int id) = 0;
};

class User : public Entity<User> {
public:
    int id;
    std::string login;
    std::string password;
    std::string firstname;
    std::string lastname;
    // Преобразование в JSON
    User get_by_id(int id) override {
        return User();
    }
    ;
    std::vector<User> get_all() override {
        return std::vector<User>();
    };
    void create(const User& entity) override {
    };
    void update(int id, const User& entity) override {
    };
    void delete_by_id(int id) override {
    };

    /*static Json::Value user_to_json(const User& user) {
        Json::Value json;
        json["id"] = user.id;
        json["login"] = user.login;
        json["firstname"] = user.firstname;
        json["lastname"] = user.lastname;
        return json;
    }
    // Функция получения всех пользователей из БД
    static std::vector<User> get_users_from_db(PGconn* conn) {
        std::vector<User> users;
        PGresult* res = PQexec(conn, "SELECT id, login, firstname, lastname FROM \"user\"");

        if (PQresultStatus(res) == PGRES_TUPLES_OK) {
            int rows = PQntuples(res);
            for (int i = 0; i < rows; i++) {
                User user;
                user.id = std::stoi(PQgetvalue(res, i, 0));
                user.login = PQgetvalue(res, i, 1);
                user.firstname = PQgetvalue(res, i, 2);
                user.lastname = PQgetvalue(res, i, 3);
                users.push_back(user);
            }
        }
        PQclear(res);
        return users;
    }

    // Функция получения пользователя по ID
     static User get_user_by_id(PGconn* conn, int user_id) {
        User user;
        std::string query = "SELECT id, login, firstname, lastname FROM \"user\" WHERE id = " + std::to_string(user_id);
        PGresult* res = PQexec(conn, query.c_str());

        if (PQresultStatus(res) == PGRES_TUPLES_OK && PQntuples(res) > 0) {
            user.id = std::stoi(PQgetvalue(res, 0, 0));
            user.login = PQgetvalue(res, 0, 1);
            user.firstname = PQgetvalue(res, 0, 2);
            user.lastname = PQgetvalue(res, 0, 3);
        }
        PQclear(res);
        return user;
    }*/
};



class News : public Entity<News> {
public:
    int id;
    int userId;
    std::string title;
    std::string content;
    std::string created;
    std::string modified;

    News get_by_id(int id) override {
        return News();
    };
    std::vector<News> get_all() override {
        return std::vector<News>();
    };
    void create(const News& entity) override {
    };
    void update(int id, const News& entity) override {
    };
    void delete_by_id(int id) override {
    };
};

Json::Value news_to_json(const News& news) {
    Json::Value json;
    json["id"] = news.id;
    json["user_id"] = news.userId;
    json["title"] = news.title;
    json["content"] = news.content;
    json["created"] = news.created;
    json["modified"] = news.modified;
    return json;
}

class Label : public Entity<Label> {
public:
    int id;
    std::string name;

    Label get_by_id(int id) override {
        return Label();
    };
    std::vector<Label> get_all() override {
        return std::vector<Label>();
    };
    void create(const Label& entity) override {
    };
    void update(int id, const Label& entity) override {
    };
    void delete_by_id(int id) override {
    };
};

Json::Value label_to_json(const Label& label) {
    Json::Value json;
    json["id"] = label.id;
    json["name"] = label.name;
    return json;
}

class Notice : Entity<Notice> {
public:
    int id;
    int newsId;
    std::string content;

    Notice get_by_id(int id) override {
        return Notice();
    };
    std::vector<Notice> get_all() override {
        return std::vector<Notice>();
    };
    void create(const Notice& entity) override {
    };
    void update(int id, const Notice& entity) override {
    };
    void delete_by_id(int id) override {
    };
};

Json::Value label_to_json(const Notice& notice) {
    Json::Value json;
    json["id"] = notice.id;
    json["newsId"] = notice.newsId;
    json["content"] = notice.content;
    return json;
}
