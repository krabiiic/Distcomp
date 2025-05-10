#pragma once
#include "Entity.hpp"

struct Comment {
    uint64_t id{};
    uint64_t articleId{};
    std::string content{};

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

        uint64_t articleId = data.contains("articleId") 
                ? static_cast<uint64_t>(data["articleId"]) 
                : 0;
        
        return {
            id, 
            articleId, 
            data["content"]
        };
    }

    static std::string get_name(){
        return "comments"s;
    }
};
