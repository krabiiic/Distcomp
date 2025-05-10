#include "LabelService.h"
#include <libpq-fe.h>   // ��� ������ � PostgreSQL
#include <iostream>
extern PGconn* conn;
std::optional<Label> LabelService::get_by_id(int id)
{
    // ������ SQL-������
    std::string query = "SELECT id, name FROM tbl_label WHERE id = " + std::to_string(id) + ";";

    // ��������� ������
    PGresult* res = PQexec(conn, query.c_str());

    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        std::cerr << "Error executing query: " << PQerrorMessage(conn) << std::endl;
        PQclear(res);
        PQfinish(conn);
        return {};  // ���������� ������ optional, ���� ������ �� �������
    }

    // ���������, ��� ��������� �� ������
    if (PQntuples(res) == 0) {
        PQclear(res);
        PQfinish(conn);
        return {};  // ���������� ������ optional, ���� ��� ������ ������������
    }

    // ��������� ������ ������������ �� ���������� �������
    Label label;
    label.id = std::stoi(PQgetvalue(res, 0, 0));
    label.name = PQgetvalue(res, 0, 1);

    // ������� ��������� � ��������� ����������
    PQclear(res);
    return label;
}

bool LabelService::exists(int id)
{
    // ������ SQL ������
    std::string query = "SELECT COUNT(*) FROM tbl_label WHERE id = " + std::to_string(id) + ";";

    // ��������� ������
    PGresult* res = PQexec(conn, query.c_str());

    // ��������� ������ ���������� �������
    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        std::cerr << "������ ���������� �������: " << PQerrorMessage(conn) << std::endl;
        PQclear(res);
        return false;
    }

    // �������� ���������� �����, ������������ ��������
    int count = std::stoi(PQgetvalue(res, 0, 0));

    // ����������� ������
    PQclear(res);

    // ���� count > 0, �� ������������ ����������
    return count > 0;
}

std::vector<Label> LabelService::get_all()
{
    std::vector<Label> labels;

    // ������ ��� ��������� ���� �������������
    PGresult* res = PQexec(conn, "SELECT id, name FROM tbl_label;");

    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        std::cerr << "������ �������: " << PQerrorMessage(conn) << std::endl;
        PQclear(res);
        return labels;
    }

    int rows = PQntuples(res);
    for (int i = 0; i < rows; i++) {
        Label label;
        label.id = std::stoi(PQgetvalue(res, i, 0));
        label.name = PQgetvalue(res, i, 1);
        labels.push_back(label);
    }

    PQclear(res);
    return labels;
}

std::optional<int> LabelService::get_id(const Label& label)
{
    // SQL ������ ��� ������ ������������ �� �����
    std::string query = "SELECT id FROM tbl_label WHERE name = '" + label.name + "';";
    std::cout << "query: " << query << std::endl;
    // ���������� ������� � �����������
    PGresult* res = PQexec(conn, query.c_str());

    if (PQresultStatus(res) == PGRES_TUPLES_OK) {
        if (PQntuples(res) > 0) {
            std::cout << "������� �������" << std::endl;
            // ���� ����� ������������, ���������� ��� ID
            int id = std::stoi(PQgetvalue(res, 0, 0));  // ����������� ������ � int
            PQclear(res);
            return std::optional<int>(id);  // ���������� ID ��� std::optional<int>
        }
    }

    // ���� ������������ �� ������, ���������� std::nullopt
    std::cerr << "������� �� �������" << std::endl;
    PQclear(res);
    return std::nullopt;  // std::nullopt - ������������ �� ������
}

bool LabelService::create(const Label& label)
{
    std::string query = "INSERT INTO tbl_label (name) VALUES('" + label.name + "') RETURNING id;";
    std::cout << query << std::endl;
    PGresult* res = PQexec(conn, query.c_str());

    if (PQresultStatus(res) == PGRES_TUPLES_OK) {
        PQclear(res);
        return true;
    }
    else {
        std::cerr << "������ ��������: " << PQerrorMessage(conn) << std::endl;
        PQclear(res);
        return false;
    }
}

bool LabelService::update(int id, const std::string& name)
{
    std::string query = "UPDATE tbl_label SET name = '" + name +
        "' WHERE id = " + std::to_string(id) + ";";
    std::cout << query << std::endl;
    // ���������� �������
    PGresult* res = PQexec(conn, query.c_str());

    // �������� ���������� ���������� �������
    if (PQresultStatus(res) == PGRES_COMMAND_OK) {
        PQclear(res);
        return true;
    }
    else {
        PQclear(res);
        return false;
    }
}

bool LabelService::delete_by_id(int id)
{
    std::string query = "DELETE FROM tbl_label WHERE id = " + std::to_string(id) + ";";
    PGresult* res = PQexec(conn, query.c_str());

    if (PQresultStatus(res) == PGRES_COMMAND_OK) {
        PQclear(res);
        return true;
    }
    else {
        std::cerr << "������ ��������: " << PQerrorMessage(conn) << std::endl;
        PQclear(res);
        return false;
    }
}
