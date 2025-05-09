#include <iostream>
#include <mongocxx/client.hpp>
#include <mongocxx/instance.hpp>
#include <mongocxx/uri.hpp>
#include <mongocxx/collection.hpp>
//#include <bsoncxx/builder/basic/document.hpp>
//#include <bsoncxx/json.hpp>
#include "Controllers/NoticeController.h"
#include "ConsumerDiscussion.h"
crow::SimpleApp app;
mongocxx::client conn{ mongocxx::uri{"mongodb://localhost:27017/"} };
mongocxx::database db;
mongocxx::collection coll;
int noticeId = 0;
std::atomic<bool> running{ true };
////////////////////////////////////////////////////////////////////////////////////////////
// Функция для обработки ввода пользователя
void console_input_handler() {
    std::string command;
    while (running) {
        //std::cout << "\nВведите команду (exit для выхода): ";
        if (std::getline(std::cin, command)) { // Ожидаем ввод
            if (command == "clear") {
                while (!ConsumerDiscussion::responseQueue.empty())
                    ConsumerDiscussion::responseQueue.pop();
                //counter.store(0);
            }
        }
    }
}
int main() {
    std::jthread input_thread(console_input_handler);
    std::jthread t1(ConsumerDiscussion::consumeInTopic);
    std::jthread t2(ConsumerDiscussion::awaitResponseFromInTopic);
    mongocxx::instance instance{};

    // Подключение к MongoDB
    

    // Выбор базы данных 'discussion'
    db = conn["discussion"];
    if (db.has_collection("tbl_notice"))
    // Выбор коллекции 'tbl_notice'
    coll = db["tbl_notice"];

    /*try {
        // Инициализация библиотеки MongoDB C++ Driver
        mongocxx::instance instance{};

        // Подключение к MongoDB
        mongocxx::client conn{ mongocxx::uri{"mongodb://localhost:27017/"} };

        // Выбор базы данных 'discussion'
        mongocxx::database db = conn["discussion"];

        // Выбор коллекции 'tbl_notice'
        mongocxx::collection coll = db["tbl_notice"];

        // Создание BSON-документа с использованием make_document
        auto document = bsoncxx::builder::basic::make_document(
            bsoncxx::builder::basic::kvp("id", int64_t(1234567890)),
            bsoncxx::builder::basic::kvp("country", "Russia"),
            bsoncxx::builder::basic::kvp("newsld", int64_t(9876543210)),
            bsoncxx::builder::basic::kvp("content", "information")
        );

        // Вставка документа в коллекцию
        auto result = coll.insert_one(document.view());

        // Проверка успешности вставки
        if (result) {
            std::cout << "Документ успешно добавлен!" << std::endl;
        }
        else {
            throw std::runtime_error("Ошибка при вставке документа");
        }
    }
    catch (const std::exception& e) {
        std::cerr << "Ошибка: " << e.what() << std::endl;
    }*/
    // Маршруты для уведомлений
    CROW_ROUTE(app, "/api/v1.0/notices").methods(crow::HTTPMethod::GET)(NoticeController::get_notices);

    CROW_ROUTE(app, "/api/v1.0/notices").methods(crow::HTTPMethod::POST)(NoticeController::create_notice);

    CROW_ROUTE(app, "/api/v1.0/notices/<int>").methods("DELETE"_method)(NoticeController::delete_notice);

    CROW_ROUTE(app, "/api/v1.0/notices/<int>").methods("GET"_method)(NoticeController::get_notice);

    CROW_ROUTE(app, "/api/v1.0/notices").methods("PUT"_method)(NoticeController::update_notice);

    app.port(24130).multithreaded().run();

    return 0;
}
