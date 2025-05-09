#include "NoticeService.h"
#include <mongocxx/client.hpp>
#include <mongocxx/instance.hpp>
#include <mongocxx/uri.hpp>
#include <mongocxx/collection.hpp>
#include <bsoncxx/builder/basic/document.hpp>
#include <bsoncxx/json.hpp>
#include <iostream>
#include <optional>
#include <vector>
#include <string>

// Инициализация клиента MongoDB
extern mongocxx::client conn;
extern mongocxx::database db;
extern mongocxx::collection coll;
extern int noticeId;
// Получить заметку по ID
std::optional<Notice> NoticeService::get_by_id(int id) {
    // Строим фильтр для поиска
    bsoncxx::builder::basic::document filter{};
    filter.append(bsoncxx::builder::basic::kvp("id", id));

    // Выполняем запрос
    auto result = coll.find_one(filter.view());

    if (!result) {
        return {};  // Если заметка не найдена, возвращаем пустой optional
    }

    // Извлекаем данные
    Notice notice;
    notice.id = result->view()["id"].get_int32().value;
    notice.newsId = result->view()["newsId"].get_int32().value;
    notice.content = std::string(result->view()["content"].get_string().value);
    std::cout << "Полученные данные:" << std::endl;
    std::cout
        << "id: " << notice.id
        << ", newsId: " << notice.newsId
        << ", content: " << notice.content <<
        std::endl;
    std::cout << "Содержимое таблицы Notice:\n";
    auto cursor = coll.find({});  // Запрашиваем все документы
    for (const auto& doc : cursor) {
        std::cout 
            << "ID: " << doc["id"].get_int32().value
            << ", NewsID: " << doc["newsId"].get_int32().value
            << ", Content: " << doc["content"].get_string().value <<
        std::endl;
    }

    return notice;
}

// Проверка существования заметки по ID
bool NoticeService::exists(int id) {
    bsoncxx::builder::basic::document filter{};
    filter.append(bsoncxx::builder::basic::kvp("id", id));

    // Выполняем запрос и проверяем, существует ли запись
    auto count = coll.count_documents(filter.view());

    return count > 0;
}

// Получить все заметки
std::vector<Notice> NoticeService::get_all() {
    std::vector<Notice> notices;

    // Выполняем запрос для получения всех документов
    auto cursor = coll.find({});

    for (auto&& doc : cursor) {
        Notice notice;
        notice.id = doc["id"].get_int32().value;
        notice.newsId = doc["newsId"].get_int32().value;
        notice.content = std::string(doc["content"].get_string().value);
        notices.push_back(notice);
    }
    std::cout << "Содержимое таблицы Notice:\n";
    auto cursor1 = coll.find({});  // Запрашиваем все документы
    for (const auto& doc : cursor1) {
        std::cout
            << "ID: " << doc["id"].get_int32().value
            << ", NewsID: " << doc["newsId"].get_int32().value
            << ", Content: " << doc["content"].get_string().value <<
            std::endl;
    }

    return notices;
}

// Получить ID заметки по newsId и content
std::optional<int> NoticeService::get_id(const Notice& notice) {
    bsoncxx::builder::basic::document filter{};
    filter.append(
        bsoncxx::builder::basic::kvp("newsId", notice.newsId),
        bsoncxx::builder::basic::kvp("content", notice.content)
    );

    // Выполняем поиск
    auto result = coll.find_one(filter.view());

    if (result) {
        // Если нашли заметку, возвращаем её ID
        return result->view()["id"].get_int32().value;
    }

    return std::nullopt;  // Если заметка не найдена, возвращаем nullopt
}

// Создать новую заметку
bool NoticeService::create(const Notice& notice) {
    bsoncxx::builder::basic::document filter{};
    filter.append(
        bsoncxx::builder::basic::kvp("newsId", notice.newsId),
        bsoncxx::builder::basic::kvp("content", notice.content)
    );

    auto existing = coll.find_one(filter.view());
   if (existing) {
        //return false;  // Такая запись уже существует, не добавляем дубликат
    }

    // Если записи нет, добавляем новую
    bsoncxx::builder::basic::document document{};
    document.append(
        bsoncxx::builder::basic::kvp("id", ++noticeId),
        bsoncxx::builder::basic::kvp("newsId", notice.newsId),
        bsoncxx::builder::basic::kvp("content", notice.content)
    );

    auto result = coll.insert_one(document.view());

    std::cout << "Содержимое таблицы Notice:\n";
    auto cursor = coll.find({});  // Запрашиваем все документы
    for (const auto& doc : cursor) {
        std::cout
            << "ID: " << doc["id"].get_int32().value
            << ", NewsID: " << doc["newsId"].get_int32().value
            << ", Content: " << doc["content"].get_string().value <<
            std::endl;
    }

    return result ? true : false;  // Возвращаем успех вставки
}

// Обновить заметку
bool NoticeService::update(int id, int newsId, const std::string& content) {
    

    // Проверяем, существует ли документ с таким id
    //auto existing_doc = coll.find_one(filter.view());
    if (!exists(id)) {
        //std::cout << "Документ с id=" << id << " не найден!" << std::endl;
        if (!exists(1 + id)) {
            //std::cout << "Документ с id=" << id << " не найден!" << std::endl;
            if (!exists(id - 1)) {
                //std::cout << "Документ с id=" << id << " не найден!" << std::endl;
                return false;
            }
            return false;
        }
        return false;
    }
    bsoncxx::builder::basic::document filter{};
    filter.append(bsoncxx::builder::basic::kvp("id", id));
    bsoncxx::builder::basic::document update{};
    update.append(
        bsoncxx::builder::basic::kvp("$set",
            bsoncxx::builder::basic::make_document(
                bsoncxx::builder::basic::kvp("newsId", newsId),
                bsoncxx::builder::basic::kvp("content", content)
            ))
    );

    // Выполняем обновление
    auto result = coll.update_one(filter.view(), update.view());

    if (!result) {
        //std::cerr << "Ошибка при обновлении документа!" << std::endl;
        return false;
    }

    // Проверяем, изменились ли данные
    if (result->modified_count() == 0) {
        //std::cout << "Данные не были изменены (возможно, они уже такие же)." << std::endl;
        return false;
    }
    std::cout << "Содержимое таблицы Notice:\n";
    auto cursor = coll.find({});  // Запрашиваем все документы
    for (const auto& doc : cursor) {
        std::cout
            << "ID: " << doc["id"].get_int32().value
            << ", NewsID: " << doc["newsId"].get_int32().value
            << ", Content: " << doc["content"].get_string().value <<
            std::endl;
    }
    //std::cout << "Документ успешно обновлен!" << std::endl;
    return true;
}


// Удалить заметку по ID
bool NoticeService::delete_by_id(int id) {
    bsoncxx::builder::basic::document filter{};
    filter.append(bsoncxx::builder::basic::kvp("id", id));

    // Выполняем удаление
    auto result = coll.delete_one(filter.view());

    std::cout << "Содержимое таблицы Notice:\n";
    auto cursor = coll.find({});  // Запрашиваем все документы
    for (const auto& doc : cursor) {
        std::cout
            << "ID: " << doc["id"].get_int32().value
            << ", NewsID: " << doc["newsId"].get_int32().value
            << ", Content: " << doc["content"].get_string().value <<
            std::endl;
    }
    bool res = result&& result->deleted_count() > 0;
    if (res)
        --noticeId;
    return res;
}
