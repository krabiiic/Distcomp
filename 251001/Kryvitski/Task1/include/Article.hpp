#pragma once
#include "Entity.hpp"

struct Article {
    uint64_t id{};
    std::string title{};
    std::string content{};
    uint64_t editorId{};
    std::tm created{};
    std::tm modified{};
    
    json to_json() const {
        return {
            {"id", id},
            {"title", title},
            {"content", content},
            {"editorId", editorId},
            {"created", time_to_iso8601(created)},
            {"modified", time_to_iso8601(modified)}
        };
    }

    static Article from_json(const std::string& data_str){
        auto data = json::parse(data_str);
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
            data["editorId"],
            created,
            modified
        };
    }

    static std::string get_name(){
        return "articles"s;
    }
};