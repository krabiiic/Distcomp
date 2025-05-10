#include "NoticeService.h"
#include <libpq-fe.h>   // ��� ������ � PostgreSQL
#include <iostream>
extern PGconn* conn;
std::optional<Notice> NoticeService::get_by_id(int id)
{
    // ������ SQL-������
    std::string query = "SELECT id, newsId, content FROM tbl_notice WHERE id = " + std::to_string(id) + ";";

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
    Notice notice;
    notice.id = std::stoi(PQgetvalue(res, 0, 0));
    notice.newsId = std::stoi(PQgetvalue(res, 0, 1));
    notice.content = PQgetvalue(res, 0, 2);

    // ������� ��������� � ��������� ����������
    PQclear(res);
    return notice;
}

bool NoticeService::exists(int id)
{
    // ������ SQL ������
    std::string query = "SELECT COUNT(*) FROM tbl_notice WHERE id = " + std::to_string(id) + ";";

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

std::vector<Notice> NoticeService::get_all()
{
    std::vector<Notice> notices;

    // ������ ��� ��������� ���� �������������
    PGresult* res = PQexec(conn, "SELECT id, newsId, content FROM tbl_notice;");

    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        std::cerr << "������ �������: " << PQerrorMessage(conn) << std::endl;
        PQclear(res);
        return notices;
    }

    int rows = PQntuples(res);
    for (int i = 0; i < rows; i++) {
        Notice notice;
        notice.id = std::stoi(PQgetvalue(res, i, 0));
        notice.newsId = std::stoi(PQgetvalue(res, i, 1));
        notice.content = PQgetvalue(res, i, 2);
        notices.push_back(notice);
        std::cout << notice.id << std::endl;
        std::cout << notice.newsId << std::endl;
    }

    PQclear(res);
    return notices;
}

std::optional<int> NoticeService::get_id(const Notice& notice)
{
    // SQL ������ ��� ������ ������������ �� �����
    std::string query = "SELECT id FROM tbl_notice WHERE newsId = " + std::to_string(notice.newsId) + " AND content = '" + notice.content + "';";
    //" AND password = " + user.password +
    //" AND firstname = " + user.firstname +
    //" AND lastname = " +  user.lastname + ";";
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

bool NoticeService::create(const Notice& notice)
{
    std::string query = "INSERT INTO tbl_notice (newsId, content) VALUES(" +
        std::to_string(notice.newsId) + ", '" + notice.content + "') RETURNING id;";
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

bool NoticeService::update(int id, int newsId, const std::string& content)
{
    std::string query = "UPDATE tbl_notice SET newsId = " + std::to_string(newsId) +
        ", content = '" + content +
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

bool NoticeService::delete_by_id(int id)
{
    std::string query = "DELETE FROM tbl_notice WHERE id = " + std::to_string(id) + ";";
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
