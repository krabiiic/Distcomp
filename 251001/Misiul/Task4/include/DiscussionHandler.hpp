#pragma once
#include <concepts>
#include <memory>
#include <vector>
#include "Entities/Entity.hpp"
#include "CassandraController.hpp"
#include <nlohmann/json.hpp>

class DiscussionHandlerBase {
public:
    explicit DiscussionHandlerBase(std::shared_ptr<CassandraController> controller) noexcept;
    virtual ~DiscussionHandlerBase() = default;

    virtual void initialize() = 0;
    virtual nlohmann::json handle_post(const nlohmann::json& payload) = 0;
    virtual nlohmann::json handle_get_all() = 0;
    virtual nlohmann::json handle_get_one(uint64_t id) = 0;
    virtual nlohmann::json handle_delete(uint64_t id) = 0;
    virtual nlohmann::json handle_put(const nlohmann::json& payload) = 0;

    const std::string& entity_name() const { return m_entity_name; }

protected:
    std::shared_ptr<CassandraController> m_controller;
    std::string m_entity_name;
};

template<CassandraEntity T>
class DiscussionHandler : public DiscussionHandlerBase {
public:
    explicit DiscussionHandler(std::shared_ptr<CassandraController> controller) noexcept;
    
    void initialize() override;
    
    nlohmann::json handle_post(const nlohmann::json& payload) override;
    nlohmann::json handle_get_all() override;
    nlohmann::json handle_get_one(uint64_t id) override;
    nlohmann::json handle_delete(uint64_t id) override;
    nlohmann::json handle_put(const nlohmann::json& payload) override;
};

inline DiscussionHandlerBase::DiscussionHandlerBase(std::shared_ptr<CassandraController> controller) noexcept
    : m_controller(controller) 
{}

template <CassandraEntity T>
DiscussionHandler<T>::DiscussionHandler(std::shared_ptr<CassandraController> controller) noexcept
    : DiscussionHandlerBase(controller) 
{
    m_entity_name = T::entity_name;
}

template <CassandraEntity T>
void DiscussionHandler<T>::initialize() {
    auto result = m_controller->create_table<T>();
}

template<CassandraEntity T>
nlohmann::json DiscussionHandler<T>::handle_post(const nlohmann::json& payload) {
    try {
        T entity = T::from_json(payload);
        if (m_controller->insert(entity)) {
            return {
                {"status", 201},
                {"data", entity.to_json()}
            };
        } else {
            return {
                {"status", 403},
                {"error", "POST error"}
            };
        }
    }
    catch (const std::exception& e) {
        return {
            {"status", 500},
            {"error", e.what()}
        };
    }
}

template<CassandraEntity T>
nlohmann::json DiscussionHandler<T>::handle_get_all() {
    try {
        std::vector<T> entities = m_controller->get_all<T>();
        nlohmann::json entities_json = nlohmann::json::array();
        
        std::ranges::transform(entities, std::back_inserter(entities_json), [](const auto& entity){
            return entity.to_json();
        });
        
        return {
            {"status", 200},
            {"data", entities_json}
        };
    } catch (const std::exception& e) {
        return {
            {"status", 500},
            {"error", e.what()}
        };
    }
}

template<CassandraEntity T>
nlohmann::json DiscussionHandler<T>::handle_get_one(uint64_t id) {
    try {
        std::optional<T> entity = m_controller->get_by_id<T>(id);
        if (entity.has_value()) {
            return {
                {"status", 200},
                {"data", entity.value().to_json()}
            };
        }
        return {
            {"status", 404},
            {"error", fmt::format("GET error: id = {} not found)", id)}
        };
    } catch (const std::exception& e) {
        return {
            {"status", 500},
            {"error", e.what()}
        };
    }
}

template<CassandraEntity T>
nlohmann::json DiscussionHandler<T>::handle_delete(uint64_t id) {
    try {
        if (m_controller->delete_by_id<T>(id)) {
            return {
                {"status", 204}
            };
        } else {
            return {
                {"status", 404},
                {"error", fmt::format("DELETE error: id = {} not found", id)}
            };
        }
    } catch (const std::exception& e) {
        return {
            {"status", 500},
            {"error", e.what()}
        };
    }
}

template<CassandraEntity T>
nlohmann::json DiscussionHandler<T>::handle_put(const nlohmann::json& payload) {
    try {
        T entity = T::from_json(payload);
        m_controller->update_by_id(entity);
        return {
            {"status", 200},
            {"data", entity.to_json()}
        };
    } catch (const std::exception& e) {
        return {
            {"status", 500},
            {"error", e.what()}
        };
    }
}