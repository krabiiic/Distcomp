#pragma once
#include <string>
#include <vector>
#include <optional>
#include "../Models/Label.h"
class LabelService {
public:
    static std::optional<Label> get_by_id(int id);
    static bool exists(int id);
    static std::vector<Label> get_all();
    static std::optional<int> get_id(const Label& label);
    static bool create(const Label& label);
    static bool update(int id, const std::string& name);
    static bool delete_by_id(int id);
};