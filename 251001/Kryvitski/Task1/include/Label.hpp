#pragma once
#include "Entity.hpp"

struct Label{
    uint64_t id{};
    std::string name{};

    json to_json() const {
        return {
            {"id", id},
            {"name", name},
        };
    }

    static Label from_json(const std::string& data_str) {
        auto data = json::parse(data_str);

        uint64_t id = data.contains("id") 
                ? static_cast<uint64_t>(data["id"]) 
                : 0;

        return {
            id, 
            data["name"]
        };
    }

    static std::string get_name(){
        return "labels"s;
    }
};
