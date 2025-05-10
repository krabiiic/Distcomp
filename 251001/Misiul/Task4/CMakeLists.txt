cmake_minimum_required(VERSION 3.20)
project(Lab4 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

find_package(httplib REQUIRED)
find_package(nlohmann_json REQUIRED)
find_package(libpqxx REQUIRED)
find_package(cassandra-cpp-driver REQUIRED)
find_package(fmt REQUIRED)
find_package(RdKafka REQUIRED)

add_executable(
    ${PROJECT_NAME}
    src/main.cpp
    src/Entity.cpp
    src/PostgresController.cpp
    src/CassandraController.cpp
    src/KafkaProducer.cpp
    src/KafkaConsumer.cpp
)

target_include_directories( ${PROJECT_NAME} PRIVATE include)

target_link_libraries(
    ${PROJECT_NAME} 
    httplib::httplib
    nlohmann_json::nlohmann_json
    libpqxx::pqxx
    fmt::fmt
    cassandra-cpp-driver::cassandra-cpp-driver
    RdKafka::rdkafka++
)