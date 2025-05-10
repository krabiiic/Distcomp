cmake_minimum_required(VERSION 3.20)
project(Lab1 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

find_package(httplib REQUIRED)
find_package(nlohmann_json REQUIRED)

add_executable(
    ${PROJECT_NAME}
    src/main.cpp
    src/Entity.cpp
)

target_include_directories( ${PROJECT_NAME} PRIVATE include)

target_link_libraries(
    ${PROJECT_NAME} 
    httplib::httplib
    nlohmann_json::nlohmann_json
)