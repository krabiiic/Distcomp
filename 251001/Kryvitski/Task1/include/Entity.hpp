#pragma once
#include <nlohmann/json.hpp>
#include <format>

using namespace std::string_literals;
using json = nlohmann::json;

std::string time_to_iso8601(const std::tm& tm);
std::tm iso8601_to_time(const std::string& iso8601);