#include "Handler.hpp"
#include <memory>


template<Entity ... Ts>
class Server final {
public:
    Server() noexcept;
    ~Server() = default;

    void start_server();

private:
    template<Entity T>
    void register_entity();

private:
    constexpr static auto SERVER_ADDRESS = "0.0.0.0";
    constexpr static uint16_t SERVER_PORT = 24110;

    httplib::Server m_server;
    std::shared_ptr<PostgresController> m_controller;
};

template<Entity ... Ts>
Server<Ts...>::Server() noexcept {
    m_controller = std::make_shared<PostgresController>();
    m_controller->initialize();
    (register_entity<Ts>(), ...);
}

template<Entity ... Ts>
template<Entity T>
void Server<Ts...>::register_entity() {
    auto handler = std::make_shared<Handler<T>>(m_controller);
    handler->initialize();
    std::string entity_name = T::entity_name;

    m_server.Post("/api/v1.0/" + entity_name, [handler](const httplib::Request& req, httplib::Response& res) {
        handler->handle_post(req, res);
    });

    m_server.Get("/api/v1.0/" + entity_name, [handler](const httplib::Request& req, httplib::Response& res) {
        handler->handle_get_all(req, res);
    });

    m_server.Get("/api/v1.0/" + entity_name + "/(\\d+)", [handler](const httplib::Request& req, httplib::Response& res) {
        uint64_t id = std::stoull(req.matches[1]);
        handler->handle_get_one(req, res, id);
    });

    m_server.Delete("/api/v1.0/" + entity_name + "/(\\d+)", [handler](const httplib::Request& req, httplib::Response& res) {
        uint64_t id = std::stoull(req.matches[1]);
        handler->handle_delete(req, res, id);
    });

    m_server.Put("/api/v1.0/" + entity_name, [handler](const httplib::Request& req, httplib::Response& res) {
        handler->handle_put(req, res);
    });
}

template<Entity ... Ts>
void Server<Ts...>::start_server() {
    m_server.listen(SERVER_ADDRESS, SERVER_PORT);
}
