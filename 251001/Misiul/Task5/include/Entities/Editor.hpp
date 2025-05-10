#pragma once
#include "Entity.hpp"

struct Editor {
    uint64_t id{};
    std::string login{};
    std::string password{};
    std::string firstname{};
    std::string lastname{};

    inline static const std::string entity_name = "creators";
    inline static const std::string table_name = "tbl_creator";

    json to_json() const {
        return {
            {"id", id},
            {"login", login},
            {"password", password},
            {"firstname", firstname},
            {"lastname", lastname}
        };
    }

    static Editor from_json(const std::string& data){
        auto json = nlohmann::json::parse(data);
        return from_json(json);
    }

    static Editor from_json(const nlohmann::json& data){
        uint64_t id = data.contains("id") 
                ? static_cast<uint64_t>(data["id"]) 
                : 0; 

        return {
            id, 
            data["login"], 
            data["password"], 
            data["firstname"], 
            data["lastname"]
        };
    }

    std::string generate_insert_query() const {
        return fmt::format(
            "INSERT INTO {} (login, password, firstname, lastname) "
            "VALUES ('{}', '{}', '{}', '{}');",
            table_name, login, password, firstname, lastname
        );
    }
    
    std::string generate_update_query() const {
        return fmt::format(
            "UPDATE {} SET "
            "login = '{}', password = '{}', firstname = '{}', lastname = '{}' WHERE id = {};",
            table_name, login, password, firstname, lastname, id
        );
    }
    
    static std::string generate_create_table() {
        return
            "CREATE TABLE tbl_creator ("
            "id BIGINT PRIMARY KEY DEFAULT 0, "
            "login VARCHAR(64) NOT NULL UNIQUE CHECK (LENGTH(login) >= 2), "
            "password VARCHAR(64) NOT NULL CHECK (LENGTH(password) >= 8), "
            "firstname VARCHAR(64) CHECK (LENGTH(firstname) >= 2), "
            "lastname VARCHAR(64) CHECK (LENGTH(lastname) >= 2)"
            "); ";
    }
    
    static Editor from_row(const pqxx::row& row) {
        return Editor{
            row["id"].as<uint64_t>(),
            row["login"].as<std::string>(),
            row["password"].as<std::string>(),
            row["firstname"].as<std::string>(),
            row["lastname"].as<std::string>()
        };
    }
};
