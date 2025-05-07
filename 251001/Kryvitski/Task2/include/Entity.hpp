#pragma once
#include <nlohmann/json.hpp>
#include <pqxx/pqxx>
#include <format>
#include <concepts>

using namespace std::string_literals;
using json = nlohmann::json;

template<typename T>
concept Entity = 
    requires (T p) {
        { p.to_json()} -> std::same_as<json>;
        { p.id } -> std::convertible_to<uint64_t>;
        { T::from_json(std::declval<const std::string&>()) } -> std::same_as<T>;
        { T::entity_name } -> std::convertible_to<std::string>;
        { T::table_name } -> std::convertible_to<std::string>;
        { p.generate_insert_query() } -> std::same_as<std::string>;
        { p.generate_update_query() } -> std::same_as<std::string>;
        { T::generate_create_table() } -> std::same_as<std::string>;
        { T::from_row(std::declval<const pqxx::row&>())} -> std::same_as<T>;
    };

std::string time_to_iso8601(const std::tm& tm);
std::tm iso8601_to_time(const std::string& iso8601);
