#include "NewsService.h"
#include <libpq-fe.h>   // ��� ������ � PostgreSQL
#include <iostream>
extern PGconn* conn;
std::optional<News> NewsService::get_by_id(int id) {
    // ������ SQL-������
    std::string query = "SELECT id, user_id, title, content FROM tbl_news WHERE id = " + std::to_string(id) + ";";

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
    News news;
    news.id = std::stoi(PQgetvalue(res, 0, 0));
    news.userId = std::stoi(PQgetvalue(res, 0, 1));
    news.title = PQgetvalue(res, 0, 2);
    news.content = PQgetvalue(res, 0, 3);

    // ������� ��������� � ��������� ����������
    PQclear(res);
    return news;
}
bool NewsService::exists(int id) {
    // ������ SQL ������
    std::string query = "SELECT COUNT(*) FROM tbl_news WHERE id = " + std::to_string(id) + ";";

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
std::vector<News> NewsService::get_all() {
    std::vector<News> newss;

    // ������ ��� ��������� ���� �������������
    PGresult* res = PQexec(conn, "SELECT id, user_id, title, content FROM tbl_news;");

    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        std::cerr << "������ �������: " << PQerrorMessage(conn) << std::endl;
        PQclear(res);
        return newss;
    }

    int rows = PQntuples(res);
    for (int i = 0; i < rows; i++) {
        News news;
        news.id = std::stoi(PQgetvalue(res, i, 0));
        news.userId = std::stoi(PQgetvalue(res, i, 1));
        news.title = PQgetvalue(res, i, 2);
        news.content = PQgetvalue(res, i, 3);
        newss.push_back(news);
        std::cout << news.id << std::endl;
        std::cout << news.userId << std::endl;
    }

    PQclear(res);
    return newss;
}
std::optional<int> NewsService::get_id(const News& news) {
    // SQL ������ ��� ������ ������������ �� �����
    std::string query = "SELECT id FROM tbl_news WHERE user_id = " + std::to_string(news.userId) + " AND title = '" + news.title + "' AND content = '" + news.content + "';";
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
bool NewsService::update(int id, int userId, const std::string& title, const std::string& content) {
    std::string query = "UPDATE tbl_news SET user_id = " + std::to_string(userId) +
        ", title = '" + title +
        "', content = '" + content +
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
bool NewsService::delete_by_id(int id) {
    std::string query = "DELETE FROM tbl_news WHERE id = " + std::to_string(id) + ";";
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
bool NewsService::create(const News& news) {
    std::string checkQuery = "SELECT COUNT(*) FROM tbl_news WHERE title = '" + news.title +
        "' AND user_id = " + std::to_string(news.userId) + ";";
    std::cout << checkQuery << std::endl;
    PGresult* checkRes = nullptr;
    std::string strEX;
    try {
        checkRes = PQexec(conn, checkQuery.c_str());
    }
    catch (std::exception &e) {
        strEX = e.what();
    }
    catch (...) {
        strEX = "unknown";
    }

    if (PQresultStatus(checkRes) != PGRES_TUPLES_OK) {
        std::cerr << "������ �������� ������������ �������: " << PQerrorMessage(conn) << std::endl;
        PQclear(checkRes);
        return false;
    }

    int count = std::stoi(PQgetvalue(checkRes, 0, 0)); // �������� ���-�� �������
    PQclear(checkRes);

    if (count > 0) {
        std::cerr << "����� ������� ��� ����������!" << std::endl;
        return false;
    }
    std::string query = "INSERT INTO tbl_news (user_id, title, content) VALUES(" +
        std::to_string(news.userId) + ", '" + news.title + "', '" + news.content + "') RETURNING id;";
    std::cout << query << std::endl;
    PGresult* res;
    try {
        res = PQexec(conn, query.c_str());
    }
    catch (std::exception& e) {
        strEX = e.what();
    }
    catch (...) {
        strEX = "unknown";
    }
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