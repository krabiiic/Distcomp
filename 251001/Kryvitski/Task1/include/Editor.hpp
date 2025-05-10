#pragma once
#include "Entity.hpp"

struct Editor {
    uint64_t id{};
    std::string login{};
    std::string password{};
    std::string firstname{};
    std::string lastname{};

    json to_json() const {
        return {
            {"id", id},
            {"login", login},
            {"password", password},
            {"firstname", firstname},
            {"lastname", lastname}
        };
    }

    static Editor from_json(const std::string& data_str){
        auto data = json::parse(data_str);

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

    static std::string get_name(){
        return "editors"s;
    }
};
