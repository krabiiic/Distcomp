#include "UserService.h"
#include <libpq-fe.h>   // ��� ������ � PostgreSQL
#include <iostream>
extern PGconn* conn;
std::vector<User> UserService::get_all()
{
    std::vector<User> users;

    // ������ ��� ��������� ���� �������������
    PGresult* res = PQexec(conn, "SELECT id, login, password, firstname, lastname FROM tbl_user;");

    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        std::cerr << "������ �������: " << PQerrorMessage(conn) << std::endl;
        PQclear(res);
        return users;
    }

    int rows = PQntuples(res);
    for (int i = 0; i < rows; i++) {
        User user;
        user.id = std::stoi(PQgetvalue(res, i, 0));
        user.login = PQgetvalue(res, i, 1);
        user.password = PQgetvalue(res, i, 2);
        user.firstname = PQgetvalue(res, i, 3);
        user.lastname = PQgetvalue(res, i, 4);
        users.push_back(user);
    }

    PQclear(res);
    return users;
}
std::optional<int> UserService::get_id(const User& user)
{
    // SQL ������ ��� ������ ������������ �� �����
    std::string query = "SELECT id FROM tbl_user WHERE login = '" + user.login + "';";
    //" AND password = " + user.password +
    //" AND firstname = " + user.firstname +
    //" AND lastname = " +  user.lastname + ";";
    std::cout << "query: " << query << std::endl;
    // ���������� ������� � �����������
    PGresult* res = PQexec(conn, query.c_str());

    if (PQresultStatus(res) == PGRES_TUPLES_OK) {
        if (PQntuples(res) > 0) {
            std::cout << "������������ ������" << std::endl;
            // ���� ����� ������������, ���������� ��� ID
            int user_id = std::stoi(PQgetvalue(res, 0, 0));  // ����������� ������ � int
            PQclear(res);
            return std::optional<int>(user_id);  // ���������� ID ��� std::optional<int>
        }
    }

    // ���� ������������ �� ������, ���������� std::nullopt
    std::cerr << "������������ �� ������" << std::endl;
    PQclear(res);
    return std::nullopt;  // std::nullopt - ������������ �� ������
}
bool UserService::exists(int user_id) {
    // ������ SQL ������
    std::string query = "SELECT COUNT(*) FROM tbl_user WHERE id = " + std::to_string(user_id) + ";";

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
std::optional<User> UserService::get_by_id(int id) {
    // ������ SQL-������
    std::string query = "SELECT id, login, password, firstname, lastname FROM tbl_user WHERE id = " + std::to_string(id) + ";";

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
    User user;
    user.id = std::stoi(PQgetvalue(res, 0, 0));  // id
    user.login = PQgetvalue(res, 0, 1);  // login
    user.password = PQgetvalue(res, 0, 2);  // password
    user.firstname = PQgetvalue(res, 0, 3);  // firstname
    user.lastname = PQgetvalue(res, 0, 4);  // lastname

    // ������� ��������� � ��������� ����������
    PQclear(res);
    return user;
}
bool UserService::create(const User& user) {
    std::string query = "INSERT INTO tbl_user (login, password, firstname, lastname) VALUES('" +
        user.login + "', '" + user.password + "', '" + user.firstname + "', '" + user.lastname + "') RETURNING id;";
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
bool UserService::update(int id, const std::string& login, const std::string& password,
    const std::string& firstname, const std::string& lastname) {
    std::string query = "UPDATE tbl_user SET login = '" + login +
        "', password = '" + password +
        "', firstname = '" + firstname +
        "', lastname = '" + lastname +
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
bool UserService::delete_by_id(int id) {
    std::string query = "DELETE FROM tbl_user WHERE id = " + std::to_string(id) + ";";
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