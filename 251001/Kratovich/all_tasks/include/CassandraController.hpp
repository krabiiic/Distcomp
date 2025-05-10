#pragma once
#include "Entities/Entity.hpp"
#include <cassandra.h>
#include <cstdint>
#include <memory>
#include <format>
#include <optional>
#include <vector>
#include <iostream>

class CassandraController {
public:
    CassandraController() noexcept;
    virtual ~CassandraController();

    CassandraController(const CassandraController&) = delete;
    CassandraController(CassandraController&&) = delete;
    CassandraController& operator=(const CassandraController&) = delete;
    CassandraController& operator=(CassandraController&&) = delete;

    bool initialize() noexcept;

    template<CassandraEntity T>
    [[nodiscard]] bool create_table() noexcept;
                    
    template<CassandraEntity T>
    [[nodiscard]] bool insert(const T& entity) noexcept;

    template<CassandraEntity T>
    std::vector<T> get_all() noexcept;

    template<CassandraEntity T>
    std::optional<T> get_by_id(uint64_t id) noexcept;

    template<CassandraEntity T>
    bool update_by_id(const T& entity) noexcept;

    template<CassandraEntity T>
    [[nodiscard]] bool delete_by_id(uint64_t id) noexcept;
    
private:
    [[nodiscard]] bool execute(const std::string& query) noexcept;
    [[nodiscard]] CassStatement* execute_query(const std::string& query) noexcept;

private:
    constexpr static auto HOST = "127.0.0.1";
    constexpr static uint16_t PORT = 9042;
    constexpr static auto SCHEMA = "distcomp";
    
    CassCluster* m_cluster = nullptr;
    CassSession* m_session = nullptr;
};


template<CassandraEntity T>
bool CassandraController::create_table() noexcept {
    std::string drop_query = std::format(
        "DROP TABLE IF EXISTS {};", 
        T::table_name
    );
    auto drop_result = execute(drop_query);

    std::string query = T::generate_create_table();
    return execute(query);
}

template<CassandraEntity T>
bool CassandraController::insert(const T& entity) noexcept {
    std::string insert_query = entity.generate_insert_query();
    return execute(insert_query);
}

template<CassandraEntity T>
std::vector<T> CassandraController::get_all() noexcept {
    std::string query = std::format("SELECT * FROM {};", T::table_name);
    CassStatement* statement = execute_query(query);
    if (!statement) return {};

    std::vector<T> entities;
    CassFuture* future = cass_session_execute(m_session, statement);
    const CassResult* result = cass_future_get_result(future);
    CassIterator* it = cass_iterator_from_result(result);

    while (cass_iterator_next(it)) {
        const CassRow* row = cass_iterator_get_row(it);
        entities.push_back(T::from_row(row));
    }

    cass_result_free(result);
    cass_iterator_free(it);
    cass_statement_free(statement);
    return entities;
}

template<CassandraEntity T>
std::optional<T> CassandraController::get_by_id(uint64_t id) noexcept {
    std::string query = std::format("SELECT * FROM {} WHERE id = {} ALLOW FILTERING;", 
        T::table_name, id);

    CassStatement* statement = execute_query(query);
    if (!statement) return {};

    CassFuture* future = cass_session_execute(m_session, statement);
    const CassResult* result = cass_future_get_result(future);
    const CassRow* row = cass_result_first_row(result);

    std::optional<T> entity{std::nullopt};
    if (row) {
        entity = T::from_row(row);
    }

    cass_result_free(result);
    cass_statement_free(statement);
    return entity;
}

template<CassandraEntity T>
bool CassandraController::update_by_id(const T& entity) noexcept {
    std::string update_query = entity.generate_update_query();
    return execute(update_query);
}

template<CassandraEntity T>
bool CassandraController::delete_by_id(uint64_t id) noexcept {
    std::string query = std::format("DELETE FROM {} WHERE country = 'belarus' "
        "AND id = {};", 
        T::table_name, id);
    return execute(query);
}

