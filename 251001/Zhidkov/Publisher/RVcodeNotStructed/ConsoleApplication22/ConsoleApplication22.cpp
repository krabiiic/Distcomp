#include <libpq-fe.h>
#include <hiredis/hiredis.h>
#include "Controllers/UserController.h"
#include "Controllers/NoticeController.h"
#include "Controllers/LabelController.h"
#include "Controllers/NewsController.h"
#include "ConsumerPublisher.h"

crow::SimpleApp app;
const char* conninfo;
PGconn* conn;
PGresult* res;
std::atomic<int> counter{ 0 };
std::atomic<bool> running{ true };
redisContext* redis_context = nullptr;
////////////////////////////////////////////////////////////////////////////////////////////
// Функция для обработки ввода пользователя
void console_input_handler() {
    std::string command;
    while(running) {
        //std::cout << "\nВведите команду (exit для выхода): ";
        if (std::getline(std::cin, command)) { // Ожидаем ввод
            if (command == "clear") {
                while (!ConsumerPublisher::responseQueue.empty())
                    ConsumerPublisher::responseQueue.pop();
                counter.store(0);
            }
        }
    }
}
////////////////////////////////////////////////////////////////////////////////////////////
int main() {
    //// Подключаемся к Redis
    redis_context = redisConnect("127.0.0.1", 6379);
    if (redis_context == nullptr || redis_context->err) {
        if (redis_context) {
            std::cerr << "Ошибка подключения: " << redis_context->errstr << std::endl;
            redisFree(redis_context);
        }
        else {
            std::cerr << "Не удалось выделить память для подключения" << std::endl;
        }
        return 1;
    }

    std::jthread input_thread(console_input_handler);
    std::jthread t1(ConsumerPublisher::consumeOutTopic);
    //SetConsoleOutputCP(65001);
    setlocale(LC_ALL, "ru");

    conninfo = "dbname=distcomp user=postgres password=postgres host=127.0.0.1 port=5432";

    // Подключаемся к базе данных
    conn = PQconnectdb(conninfo);

    // Проверяем успешность подключения
    if (PQstatus(conn) != CONNECTION_OK) {
        std::cerr << "Ошибка подключения: " << PQerrorMessage(conn) << std::endl;
        PQfinish(conn);
        return 1;
    }
    
    // Маршруты для пользователей
    CROW_ROUTE(app, "/api/v1.0/users").methods(crow::HTTPMethod::GET)(UserController::get_users);

    CROW_ROUTE(app, "/api/v1.0/users").methods(crow::HTTPMethod::POST)(UserController::create_user);

    CROW_ROUTE(app, "/api/v1.0/users/<int>").methods("DELETE"_method)(UserController::delete_user);

    CROW_ROUTE(app, "/api/v1.0/users/<int>").methods(crow::HTTPMethod::GET)(UserController::get_user);

    CROW_ROUTE(app, "/api/v1.0/users").methods(crow::HTTPMethod::PUT)(UserController::update_user);
    

    // Маршруты для новостей
    CROW_ROUTE(app, "/api/v1.0/news").methods(crow::HTTPMethod::GET)(NewsController::get_newss);

    CROW_ROUTE(app, "/api/v1.0/news").methods(crow::HTTPMethod::POST)(NewsController::create_news);

    CROW_ROUTE(app, "/api/v1.0/news/<int>").methods("DELETE"_method)(NewsController::delete_news);

    CROW_ROUTE(app, "/api/v1.0/news/<int>").methods(crow::HTTPMethod::GET)(NewsController::get_news);

    CROW_ROUTE(app, "/api/v1.0/news").methods(crow::HTTPMethod::PUT)(NewsController::update_news);
    

    // Маршруты для уведомлений
    CROW_ROUTE(app, "/api/v1.0/notices").methods(crow::HTTPMethod::GET)(NoticeController::get_notices);

    CROW_ROUTE(app, "/api/v1.0/notices").methods(crow::HTTPMethod::POST)(NoticeController::create_notice);

    CROW_ROUTE(app, "/api/v1.0/notices/<int>").methods("DELETE"_method)(NoticeController::delete_notice);

    CROW_ROUTE(app, "/api/v1.0/notices/<int>").methods("GET"_method)(NoticeController::get_notice);

    CROW_ROUTE(app, "/api/v1.0/notices").methods("PUT"_method)(NoticeController::update_notice);

    
    // Маршруты для меток
    CROW_ROUTE(app, "/api/v1.0/labels").methods(crow::HTTPMethod::GET)(LabelController::get_labels);

    CROW_ROUTE(app, "/api/v1.0/labels").methods(crow::HTTPMethod::POST)(LabelController::create_label);

    CROW_ROUTE(app, "/api/v1.0/labels/<int>").methods("DELETE"_method)(LabelController::delete_label);

    CROW_ROUTE(app, "/api/v1.0/labels/<int>").methods(crow::HTTPMethod::GET)(LabelController::get_label);

    CROW_ROUTE(app, "/api/v1.0/labels").methods(crow::HTTPMethod::PUT)(LabelController::update_label);
    
    app.port(24110).multithreaded().run();
    PQfinish(conn);
    //CROW_LOG_INFO << "counter: " << counter;
    return 0;
}


