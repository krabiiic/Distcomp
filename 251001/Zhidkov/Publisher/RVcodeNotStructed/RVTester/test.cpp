#include "pch.h"
#include "CurlHttpClient.h"

TEST(CurlHttpClientTest, GetRequest) {
    CurlHttpClient client;
    std::string response = client.get("http://localhost:24110/api/v1.0/users");
    ASSERT_TRUE(true) << response;
    //std::cout << response << std::endl;
}
TEST(CurlHttpClientTest, PostDeleteRequest) {
    CurlHttpClient client;
    std::string json_data = R"({
        "login": "user3204",
        "password": "asdfghj6047",
        "firstname": "firstname6711",
        "lastname": "lastname551"
    })";
    std::string response = client.post("http://localhost:24110/api/v1.0/users", json_data);

    ASSERT_FALSE(response.empty()) << "Ответ пуст!";
    ASSERT_TRUE(true) << response;
    //std::cout << response << std::endl;
    std::size_t id_pos = response.find("\"id\":");
    if (id_pos == std::string::npos) {
        //std::cerr << "Ошибка: поле 'id' не найдено" << std::endl;
    }
    id_pos += 5; 
    std::size_t end_pos = response.find_first_not_of("0123456789", id_pos);
    int id = std::stoi(response.substr(id_pos, end_pos - id_pos));
    //std::cout << id << std::endl;
    response = client.deleteRequest("http://localhost:24110/api/v1.0/users", id);
    //std::cout << response << std::endl;
}