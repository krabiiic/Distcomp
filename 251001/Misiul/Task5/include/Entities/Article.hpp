#pragma once
#include "Entity.hpp"

struct Article {
    uint64_t id{};
    std::string title{};
    std::string content{};
    uint64_t editorId{};
    std::tm created{};
    std::tm modified{};

    inline static const std::string entity_name = "topics";
    inline static const std::string table_name = "tbl_topic"; 
    
    json to_json() const {
        return {
            {"id", id},
            {"title", title},
            {"content", content},
            {"creatorId", editorId},
            {"created", time_to_iso8601(created)},
            {"modified", time_to_iso8601(modified)}
        };
    }

    static Article from_json(const std::string& data){
        auto json = nlohmann::json::parse(data);
        return from_json(json);
    }

    static Article from_json(const nlohmann::json& data){
        std::tm now = {};
        std::time_t t = std::time(nullptr);
        gmtime_r(&t, &now);

        uint64_t id = data.contains("id") 
                ? static_cast<uint64_t>(data["id"]) 
                : 0;

        std::tm created = data.contains("created") 
                ? iso8601_to_time(data["created"]) 
                : now;

        std::tm modified = data.contains("modified") 
                ? iso8601_to_time(data["modified"]) 
                : now;

        return {
            id, 
            data["title"],
            data["content"], 
            data["creatorId"],
            created,
            modified
        };
    }

    std::string generate_insert_query() const {
        return fmt::format(
            "INSERT INTO {} (title, content, creator_id, created, modified) "
            "VALUES ('{}', '{}', {}, '{}', '{}');",
            table_name, title, content, editorId, 
            time_to_iso8601(created), time_to_iso8601(modified)
        );
    }
    
    std::string generate_update_query() const {
        return fmt::format(
            "UPDATE {} SET title = '{}', content = '{}', "
            "creator_id = {}, modified = '{}' WHERE id = {}",
            table_name, title, content, editorId, 
            time_to_iso8601(modified), id
        );
    }
    
    static std::string generate_create_table() {
        return
            "CREATE TABLE tbl_topic ("
            "id BIGINT PRIMARY KEY DEFAULT 0, "
            "title VARCHAR(64) UNIQUE NOT NULL CHECK (LENGTH(title) >= 2), "
            "content TEXT NOT NULL CHECK (LENGTH(content) >= 4), "
            "creator_id BIGINT NOT NULL REFERENCES tbl_creator(id), "
            "created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
            "modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"
            "); ";
    }
    
    static Article from_row(const pqxx::row& row) {
        return Article{
            row["id"].as<uint64_t>(),
            row["title"].as<std::string>(),
            row["content"].as<std::string>(),
            row["creator_id"].as<uint64_t>(),
            iso8601_to_time(row["created"].as<std::string>()),
            iso8601_to_time(row["modified"].as<std::string>())
        };
    }
};