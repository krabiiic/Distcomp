#pragma once
#include "Entity.hpp"

struct Label{
    uint64_t id{};
    std::string name{};

    inline static const std::string entity_name = "labels"s;
    inline static const std::string table_name = "tbl_label"s; 

    json to_json() const {
        return {
            {"id", id},
            {"name", name},
        };
    }

    static Label from_json(const std::string& data){
        auto json = nlohmann::json::parse(data);
        return from_json(json);
    }

    static Label from_json(const nlohmann::json& data) {
        uint64_t id = data.contains("id") 
                ? static_cast<uint64_t>(data["id"]) 
                : 0;

        return {
            id, 
            data["name"]
        };
    }

    std::string generate_insert_query() const {
        return std::format(
            "INSERT INTO {} (name) "
            "VALUES ('{}');",
            table_name, name
        );
    }

    std::string generate_update_query() const {
        return std::format(
            "UPDATE {} SET name = '{}' WHERE id = {}",
            table_name, name, id
        );
    }

    static std::string generate_create_table() {
        return
            "CREATE TABLE tbl_label ("
            "id BIGINT PRIMARY KEY DEFAULT 0, "
            "name VARCHAR(32) NOT NULL CHECK (LENGTH(name) >= 2)"
            "); ";
    }

    static Label from_row(const pqxx::row& row) {
        return Label {
            row["id"].as<uint64_t>(),
            row["name"].as<std::string>()
        };
    }
};
