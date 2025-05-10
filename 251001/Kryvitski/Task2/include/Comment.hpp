#pragma once
#include "Entity.hpp"

struct Comment {
    uint64_t id{};
    uint64_t articleId{};
    std::string content{};

    inline static const std::string entity_name = "comments"s;
    inline static const std::string table_name = "tbl_comment"s; 

    json to_json() const {
        return {
            {"id", id},
            {"articleId", articleId},
            {"content", content},
        };
    }
    
    static Comment from_json(const std::string& data_str){
        auto data = json::parse(data_str);

        uint64_t id = data.contains("id") 
                ? static_cast<uint64_t>(data["id"]) 
                : 0;

        return {
            id, 
            data["articleId"], 
            data["content"]
        };
    }

    std::string generate_insert_query() const {
        return std::format(
            "INSERT INTO {} (article_id, content) "
            "VALUES ({}, '{}');",
            table_name, articleId, content
        );
    }
    
    std::string generate_update_query() const {
        return std::format(
            "UPDATE {} SET article_id = {}, "
            "content = '{}' WHERE id = {}",
            table_name, articleId, content, id
        );
    }
    
    static std::string generate_create_table() {
        return
            "CREATE TABLE tbl_comment ("
            "id BIGINT PRIMARY KEY DEFAULT 0, "
            "article_id BIGINT NOT NULL REFERENCES tbl_article(id), "
            "content TEXT NOT NULL CHECK (LENGTH(content) >= 2)"
            "); ";
    }
    
    static Comment from_row(const pqxx::row& row) {
        return Comment{
            row["id"].as<uint64_t>(),
            row["article_id"].as<uint64_t>(),
            row["content"].as<std::string>(),
        };
    }
};
