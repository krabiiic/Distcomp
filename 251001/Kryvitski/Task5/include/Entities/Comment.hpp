#pragma once
#include "Entity.hpp"

struct Comment {
    std::string country{"belarus"}; 
    int64_t articleId{}; 
    int64_t id{};           
    std::string content{};  

    inline static const std::string entity_name = "comments"s;
    inline static const std::string table_name = "tbl_comment"s;

    json to_json() const {
        return {
            {"country", country},
            {"articleId", articleId},
            {"id", id},
            {"content", content}
        };
    }

    static Comment from_json(const std::string& data){
        auto json = nlohmann::json::parse(data);
        return from_json(json);
    }

    static Comment from_json(const nlohmann::json& data) {
        return {
            data.value("country", "belarus"),
            data.value("articleId", 0),
            data.value("id", 0),
            data.value("content", "")
        };
    }

    std::string generate_insert_query() const {
        return std::format(
            "INSERT INTO {} (country, article_id, id, content) "
            "VALUES ('{}', {}, {}, '{}');",
            table_name, country, articleId, id, content
        );
    }

    std::string generate_update_query() const {
        return std::format(
            "UPDATE {} SET content = '{}' "
            "WHERE country = '{}' AND id = {};",
            table_name, content, country, id
        );
    }

    static std::string generate_create_table() {
        return std::format(
            "CREATE TABLE IF NOT EXISTS {} ("
            "country TEXT, "
            "article_id BIGINT, "
            "id BIGINT, "
            "content TEXT, "
            "PRIMARY KEY (country, id)"
            ") WITH CLUSTERING ORDER BY (id ASC);",
            table_name
        );
    }

    static Comment from_row(const CassRow* row) {
        Comment comment;
        const CassValue* country_value = cass_row_get_column_by_name(row, "country");
        const CassValue* article_value = cass_row_get_column_by_name(row, "article_id");
        const CassValue* id_value = cass_row_get_column_by_name(row, "id");
        const CassValue* content_value = cass_row_get_column_by_name(row, "content");

        const char* country_str;
        size_t country_length;
        if (cass_value_get_string(country_value, &country_str, &country_length) == CASS_OK) {
            comment.country.assign(country_str, country_length);
        }

        cass_value_get_int64(article_value, &comment.articleId);
        cass_value_get_int64(id_value, &comment.id);

        const char* content_str;
        size_t content_length;
        if (cass_value_get_string(content_value, &content_str, &content_length) == CASS_OK) {
            comment.content.assign(content_str, content_length);
        }

        return comment;
    }
};