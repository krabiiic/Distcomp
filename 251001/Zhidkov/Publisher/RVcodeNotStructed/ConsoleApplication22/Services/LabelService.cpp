#include "LabelService.h"
#include <libpq-fe.h>   // Для работы с PostgreSQL
#include <iostream>
extern PGconn* conn;
std::optional<Label> LabelService::get_by_id(int id)
{
    // Строим SQL-запрос
    std::string query = "SELECT id, name FROM tbl_label WHERE id = " + std::to_string(id) + ";";

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
    Label label;
    label.id = std::stoi(PQgetvalue(res, 0, 0));
    label.name = PQgetvalue(res, 0, 1);

    // Очищаем результат и завершаем соединение
    PQclear(res);
    return label;
}

bool LabelService::exists(int id)
{
    // Строим SQL запрос
    std::string query = "SELECT COUNT(*) FROM tbl_label WHERE id = " + std::to_string(id) + ";";

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

std::vector<Label> LabelService::get_all()
{
    std::vector<Label> labels;

    // Запрос для получения всех пользователей
    PGresult* res = PQexec(conn, "SELECT id, name FROM tbl_label;");

    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        std::cerr << "Ошибка запроса: " << PQerrorMessage(conn) << std::endl;
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
    // SQL запрос для поиска пользователя по полям
    std::string query = "SELECT id FROM tbl_label WHERE name = '" + label.name + "';";
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
        std::cerr << "Ошибка создания: " << PQerrorMessage(conn) << std::endl;
        PQclear(res);
        return false;
    }
}

bool LabelService::update(int id, const std::string& name)
{
    std::string query = "UPDATE tbl_label SET name = '" + name +
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

bool LabelService::delete_by_id(int id)
{
    std::string query = "DELETE FROM tbl_label WHERE id = " + std::to_string(id) + ";";
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
