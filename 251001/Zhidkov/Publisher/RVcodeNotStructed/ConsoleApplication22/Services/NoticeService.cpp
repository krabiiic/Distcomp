#include "NoticeService.h"
#include <libpq-fe.h>   // Для работы с PostgreSQL
#include <iostream>
extern PGconn* conn;
std::optional<Notice> NoticeService::get_by_id(int id)
{
    // Строим SQL-запрос
    std::string query = "SELECT id, newsId, content FROM tbl_notice WHERE id = " + std::to_string(id) + ";";

    // Выполняем запрос
    PGresult* res = PQexec(conn, query.c_str());

    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        std::cerr << "Error executing query: " << PQerrorMessage(conn) << std::endl;
        PQclear(res);
        PQfinish(conn);
        return {};  // Возвращаем пустой optional, если запрос не успешен
    }

    // Проверяем, что результат не пустой
    if (PQntuples(res) == 0) {
        PQclear(res);
        PQfinish(conn);
        return {};  // Возвращаем пустой optional, если нет такого пользователя
    }

    // Извлекаем данные пользователя из результата запроса
    Notice notice;
    notice.id = std::stoi(PQgetvalue(res, 0, 0));
    notice.newsId = std::stoi(PQgetvalue(res, 0, 1));
    notice.content = PQgetvalue(res, 0, 2);

    // Очищаем результат и завершаем соединение
    PQclear(res);
    return notice;
}

bool NoticeService::exists(int id)
{
    // Строим SQL запрос
    std::string query = "SELECT COUNT(*) FROM tbl_notice WHERE id = " + std::to_string(id) + ";";

    // Выполняем запрос
    PGresult* res = PQexec(conn, query.c_str());

    // Проверяем статус выполнения запроса
    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        std::cerr << "Ошибка выполнения запроса: " << PQerrorMessage(conn) << std::endl;
        PQclear(res);
        return false;
    }

    // Получаем количество строк, возвращённых запросом
    int count = std::stoi(PQgetvalue(res, 0, 0));

    // Освобождаем память
    PQclear(res);

    // Если count > 0, то пользователь существует
    return count > 0;
}

std::vector<Notice> NoticeService::get_all()
{
    std::vector<Notice> notices;

    // Запрос для получения всех пользователей
    PGresult* res = PQexec(conn, "SELECT id, newsId, content FROM tbl_notice;");

    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        std::cerr << "Ошибка запроса: " << PQerrorMessage(conn) << std::endl;
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
    // SQL запрос для поиска пользователя по полям
    std::string query = "SELECT id FROM tbl_notice WHERE newsId = " + std::to_string(notice.newsId) + " AND content = '" + notice.content + "';";
    //" AND password = " + user.password +
    //" AND firstname = " + user.firstname +
    //" AND lastname = " +  user.lastname + ";";
    std::cout << "query: " << query << std::endl;
    // Выполнение запроса с параметрами
    PGresult* res = PQexec(conn, query.c_str());

    if (PQresultStatus(res) == PGRES_TUPLES_OK) {
        if (PQntuples(res) > 0) {
            std::cout << "Заметка найдена" << std::endl;
            // Если нашли пользователя, возвращаем его ID
            int id = std::stoi(PQgetvalue(res, 0, 0));  // Преобразуем строку в int
            PQclear(res);
            return std::optional<int>(id);  // Возвращаем ID как std::optional<int>
        }
    }

    // Если пользователь не найден, возвращаем std::nullopt
    std::cerr << "Заметка не найдена" << std::endl;
    PQclear(res);
    return std::nullopt;  // std::nullopt - пользователь не найден
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
        std::cerr << "Ошибка создания: " << PQerrorMessage(conn) << std::endl;
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
    // Выполнение запроса
    PGresult* res = PQexec(conn, query.c_str());

    // Проверка успешности выполнения запроса
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
        std::cerr << "Ошибка удаления: " << PQerrorMessage(conn) << std::endl;
        PQclear(res);
        return false;
    }
}
