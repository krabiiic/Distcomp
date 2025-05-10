#pragma once
#include "Entities/Entity.hpp"
#include <pqxx/pqxx>
#include <cstdint>
#include <memory>
#include <format>
#include <optional>

using namespace std::string_literals;

class PostgresController {
public:
    PostgresController() noexcept = default;
    virtual ~PostgresController();

    PostgresController(const PostgresController&) = delete;
    PostgresController(PostgresController&&) = delete;
    PostgresController& operator=(const PostgresController&) = delete;
    PostgresController& operator=(PostgresController&&) = delete;

    bool initialize();

    template<PostgresEntity T>
    [[nodiscard]] bool create_table();
                    
    template<PostgresEntity T>
    [[nodiscard]] bool insert(const T& entity);

    template<PostgresEntity T>
    std::vector<T> get_all();

    template<PostgresEntity T>
    std::optional<T> get_by_id(uint64_t id);

    template<PostgresEntity T>
    void update_by_id(const T& entity);

    template<PostgresEntity T>
    [[nodiscard]] bool delete_by_id(uint64_t id);
    
private:
    [[nodiscard]] pqxx::result execute(const std::string& query) noexcept;

private:
    constexpr static auto DRIVER_PREFIX = "jdbc:postgresql:";
    constexpr static auto HOST = "localhost";
    constexpr static uint16_t PORT = 5432;
    constexpr static auto USER = "postgres";
    constexpr static auto PASSWORD = "postgres";
    constexpr static auto SCEME = "distcomp";
    
    std::unique_ptr<pqxx::connection> m_connection;
};

template<PostgresEntity T>
std::vector<T> PostgresController::get_all() {
    std::string query = std::format(
        "SELECT * FROM {};", 
        T::table_name
    );
    auto rows = execute(query);

    std::vector<T> entities;
    std::ranges::transform(rows, std::back_inserter(entities), [](const auto& row){
        return T::from_row(row);
    });
    return entities;
}

template<PostgresEntity T>
std::optional<T> PostgresController::get_by_id(uint64_t id) {
    std::string query = std::format(
        "SELECT * FROM {} WHERE id = {};", 
        T::table_name,
        id
    );
    auto result = execute(query);
    if (result.empty()) {
        return {};
    }
    return T::from_row(result[0]);
}

template<PostgresEntity T>
bool PostgresController::create_table() 
{
    std::string drop_query = std::format(
        "DROP TABLE IF EXISTS {} CASCADE;", 
        T::table_name
    );
    auto drop_result = execute(drop_query);

    std::string query = T::generate_create_table();
    auto result = execute(query);
    return true;
}

template<PostgresEntity T>
bool PostgresController::insert(const T& entity) 
{
    std::string insert_query = entity.generate_insert_query();
    auto result = execute(insert_query);
    return result.affected_rows() > 0;
}

template<PostgresEntity T>
void PostgresController::update_by_id(const T& entity) 
{
    std::string update_query = entity.generate_update_query();
    auto result = execute(update_query);
}

template<PostgresEntity T>
bool PostgresController::delete_by_id(uint64_t id) 
{
    std::string query = std::format(
        "DELETE FROM {} WHERE id = {};", 
        T::table_name,
        id
    );
    auto result = execute(query);
    return result.affected_rows() > 0;
}
