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

// ������������� ������� MongoDB
extern mongocxx::client conn;
extern mongocxx::database db;
extern mongocxx::collection coll;
extern int noticeId;
// �������� ������� �� ID
std::optional<Notice> NoticeService::get_by_id(int id) {
    // ������ ������ ��� ������
    bsoncxx::builder::basic::document filter{};
    filter.append(bsoncxx::builder::basic::kvp("id", id));

    // ��������� ������
    auto result = coll.find_one(filter.view());

    if (!result) {
        return {};  // ���� ������� �� �������, ���������� ������ optional
    }

    // ��������� ������
    Notice notice;
    notice.id = result->view()["id"].get_int32().value;
    notice.newsId = result->view()["newsId"].get_int32().value;
    notice.content = std::string(result->view()["content"].get_string().value);
    std::cout << "���������� ������:" << std::endl;
    std::cout
        << "id: " << notice.id
        << ", newsId: " << notice.newsId
        << ", content: " << notice.content <<
        std::endl;
    std::cout << "���������� ������� Notice:\n";
    auto cursor = coll.find({});  // ����������� ��� ���������
    for (const auto& doc : cursor) {
        std::cout 
            << "ID: " << doc["id"].get_int32().value
            << ", NewsID: " << doc["newsId"].get_int32().value
            << ", Content: " << doc["content"].get_string().value <<
        std::endl;
    }

    return notice;
}

// �������� ������������� ������� �� ID
bool NoticeService::exists(int id) {
    bsoncxx::builder::basic::document filter{};
    filter.append(bsoncxx::builder::basic::kvp("id", id));

    // ��������� ������ � ���������, ���������� �� ������
    auto count = coll.count_documents(filter.view());

    return count > 0;
}

// �������� ��� �������
std::vector<Notice> NoticeService::get_all() {
    std::vector<Notice> notices;

    // ��������� ������ ��� ��������� ���� ����������
    auto cursor = coll.find({});

    for (auto&& doc : cursor) {
        Notice notice;
        notice.id = doc["id"].get_int32().value;
        notice.newsId = doc["newsId"].get_int32().value;
        notice.content = std::string(doc["content"].get_string().value);
        notices.push_back(notice);
    }
    std::cout << "���������� ������� Notice:\n";
    auto cursor1 = coll.find({});  // ����������� ��� ���������
    for (const auto& doc : cursor1) {
        std::cout
            << "ID: " << doc["id"].get_int32().value
            << ", NewsID: " << doc["newsId"].get_int32().value
            << ", Content: " << doc["content"].get_string().value <<
            std::endl;
    }

    return notices;
}

// �������� ID ������� �� newsId � content
std::optional<int> NoticeService::get_id(const Notice& notice) {
    bsoncxx::builder::basic::document filter{};
    filter.append(
        bsoncxx::builder::basic::kvp("newsId", notice.newsId),
        bsoncxx::builder::basic::kvp("content", notice.content)
    );

    // ��������� �����
    auto result = coll.find_one(filter.view());

    if (result) {
        // ���� ����� �������, ���������� � ID
        return result->view()["id"].get_int32().value;
    }

    return std::nullopt;  // ���� ������� �� �������, ���������� nullopt
}

// ������� ����� �������
bool NoticeService::create(const Notice& notice) {
    bsoncxx::builder::basic::document filter{};
    filter.append(
        bsoncxx::builder::basic::kvp("newsId", notice.newsId),
        bsoncxx::builder::basic::kvp("content", notice.content)
    );

    auto existing = coll.find_one(filter.view());
   if (existing) {
        //return false;  // ����� ������ ��� ����������, �� ��������� ��������
    }

    // ���� ������ ���, ��������� �����
    bsoncxx::builder::basic::document document{};
    document.append(
        bsoncxx::builder::basic::kvp("id", ++noticeId),
        bsoncxx::builder::basic::kvp("newsId", notice.newsId),
        bsoncxx::builder::basic::kvp("content", notice.content)
    );

    auto result = coll.insert_one(document.view());

    std::cout << "���������� ������� Notice:\n";
    auto cursor = coll.find({});  // ����������� ��� ���������
    for (const auto& doc : cursor) {
        std::cout
            << "ID: " << doc["id"].get_int32().value
            << ", NewsID: " << doc["newsId"].get_int32().value
            << ", Content: " << doc["content"].get_string().value <<
            std::endl;
    }

    return result ? true : false;  // ���������� ����� �������
}

// �������� �������
bool NoticeService::update(int id, int newsId, const std::string& content) {
    

    // ���������, ���������� �� �������� � ����� id
    //auto existing_doc = coll.find_one(filter.view());
    if (!exists(id)) {
        //std::cout << "�������� � id=" << id << " �� ������!" << std::endl;
        if (!exists(1 + id)) {
            //std::cout << "�������� � id=" << id << " �� ������!" << std::endl;
            if (!exists(id - 1)) {
                //std::cout << "�������� � id=" << id << " �� ������!" << std::endl;
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

    // ��������� ����������
    auto result = coll.update_one(filter.view(), update.view());

    if (!result) {
        //std::cerr << "������ ��� ���������� ���������!" << std::endl;
        return false;
    }

    // ���������, ���������� �� ������
    if (result->modified_count() == 0) {
        //std::cout << "������ �� ���� �������� (��������, ��� ��� ����� ��)." << std::endl;
        return false;
    }
    std::cout << "���������� ������� Notice:\n";
    auto cursor = coll.find({});  // ����������� ��� ���������
    for (const auto& doc : cursor) {
        std::cout
            << "ID: " << doc["id"].get_int32().value
            << ", NewsID: " << doc["newsId"].get_int32().value
            << ", Content: " << doc["content"].get_string().value <<
            std::endl;
    }
    //std::cout << "�������� ������� ��������!" << std::endl;
    return true;
}


// ������� ������� �� ID
bool NoticeService::delete_by_id(int id) {
    bsoncxx::builder::basic::document filter{};
    filter.append(bsoncxx::builder::basic::kvp("id", id));

    // ��������� ��������
    auto result = coll.delete_one(filter.view());

    std::cout << "���������� ������� Notice:\n";
    auto cursor = coll.find({});  // ����������� ��� ���������
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
