#pragma once
#include <httplib.h>
#include "Article.hpp"
#include "Editor.hpp"
#include <concepts>

std::string time_to_iso8601(const std::tm& tm);
std::tm iso8601_to_time(const std::string& iso8601);

template<typename T>
concept Entity = 
    requires (T p) {
        p.to_json();
        //{ p.id } -> std::same_as<uint64_t>;
        { T::from_json(std::declval<const std::string&>()) } -> std::same_as<T>;
        { T::get_name() } -> std::same_as<std::string>;
    };
    

template<Entity T>
class Handler {
public:
    Handler() noexcept = default;
    ~Handler() = default;

    void handle_post(const httplib::Request& req, httplib::Response& res);
    void handle_get_all(const httplib::Request& req, httplib::Response& res);
    void handle_get_one(const httplib::Request& req, httplib::Response& res, uint64_t id);
    void handle_delete(const httplib::Request& req, httplib::Response& res, uint64_t id);
    void handle_put(const httplib::Request& req, httplib::Response& res);

private:
    uint64_t last_id{1};
    std::vector<T> entities{};
};

template<Entity T>
void Handler<T>::handle_post(const httplib::Request& req, httplib::Response& res) {
    auto entity = T::from_json(req.body);
    if (entity.id == 0){
        entity.id = last_id++;
    }
    entities.push_back(entity);

    res.status = 201;
    res.set_content(entity.to_json().dump(), "application/json");
}

template<Entity T>
void Handler<T>::handle_get_all(const httplib::Request& req, httplib::Response& res) {
    json entities_json = json::array();
    std::ranges::transform(entities, std::back_inserter(entities_json), 
        [this](const T& entity)
    {
        return entity.to_json();
    });

    res.status = 200;
    res.set_content(entities_json.dump(), "application/json");
}

template<Entity T>
void Handler<T>::handle_get_one(const httplib::Request& req, httplib::Response& res, uint64_t id) {
    auto it = std::ranges::find_if(entities, [id](const T& entity) { 
        return entity.id == id; 
    });

    if (it != entities.end()) {
        res.status = 200;
        res.set_content(it->to_json().dump(), "application/json");
    } else {
        res.status = 404;
        res.set_content(json{{"error", std::format("Id = {} not found", id)}}.dump(), "application/json");
    }
}

template<Entity T>
void Handler<T>::handle_delete(const httplib::Request& req, httplib::Response& res, uint64_t id) {
    auto it = std::ranges::find_if(entities, [id](const T& entity) { 
        return entity.id == id; 
    });

    if (it != entities.end()) {
        entities.erase(it);
        res.status = 204;
    } else {
        res.status = 404;
        res.set_content(json{{"error", std::format("Id = {} not found", id)}}.dump(), "application/json");
    }
}

template<Entity T>
void Handler<T>::handle_put(const httplib::Request& req, httplib::Response& res) {
    auto entity = T::from_json(req.body);
    
    uint64_t id = entity.id;
    auto it = std::ranges::find_if(entities, [id](const T& entity) { 
        return entity.id == id; 
    });
    
    if (it != entities.end()) {
        *it = entity;
        res.status = 200;
        res.set_content(it->to_json().dump(), "application/json");
    } else {
        res.status = 404;
        res.set_content(json{{"error", std::format("Id = {} not found", id)}}.dump(), "application/json");
    }
}

template<>
inline void Handler<Editor>::handle_put(const httplib::Request& req, httplib::Response& res) {
    auto entity = Editor::from_json(req.body);
    if (entity.login.size() <= 1) {
        res.status = 404;
        res.set_content(json{{"error", "Invalid title"}}.dump(), "application/json");
        return;
    }
    
    uint64_t id = entity.id;
    auto it = std::ranges::find_if(entities, [id](const Editor& entity) { 
        return entity.id == id; 
    });
    
    if (it != entities.end()) {
        *it = entity;
        res.status = 200;
        res.set_content(it->to_json().dump(), "application/json");
    } else {
        res.status = 404;
        res.set_content(json{{"error", std::format("Id = {} not found", id)}}.dump(), "application/json");
    }
}

template<>
inline void Handler<Article>::handle_put(const httplib::Request& req, httplib::Response& res) {
    auto entity = Article::from_json(req.body);
    
    if (entity.title.size() <= 1) {
        res.status = 404;
        res.set_content(json{{"error", "Invalid title"}}.dump(), "application/json");
        return;
    }

    uint64_t id = entity.id;
    auto it = std::ranges::find_if(entities, [id](const Article& entity) { 
        return entity.id == id; 
    });
    
    if (it != entities.end()) {
        *it = entity;
        res.status = 200;
        res.set_content(it->to_json().dump(), "application/json");
    } else {
        res.status = 404;
        res.set_content(json{{"error", std::format("Id = {} not found", id)}}.dump(), "application/json");
    }
}