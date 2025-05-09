#pragma once
#ifndef CURL_HTTP_CLIENT_H
#define CURL_HTTP_CLIENT_H

#include <curl/curl.h>
#include <string>

class CurlHttpClient {
public:
    CurlHttpClient();
    ~CurlHttpClient();

    std::string get(const std::string& url);
    std::string getById(const std::string& url, int id);
    std::pair<int, std::string> post(const std::string& url, const std::string& data);
    std::string put(const std::string& url, int id, const std::string& data);
    std::string deleteRequest(const std::string& url, int id);

private:
    static size_t WriteCallback(void* contents, size_t size, size_t nmemb, void* userp);
};


#endif

