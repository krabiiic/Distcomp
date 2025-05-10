#include "PublisherServer.hpp"
#include "DiscussionServer.hpp"
#include "Entities/Editor.hpp"
#include "Entities/Article.hpp"
#include "Entities/Comment.hpp"
#include "Entities/Label.hpp"
#include <thread>

int main() {
    PublisherServer<Editor, Article, Label> publisher;
    DiscussionServer<Comment> discussion;

    auto discussion_thread = std::jthread([&](){
        discussion.start_server();
    });
    publisher.redirect_to_discussion<Comment>(); 
    publisher.start_server();
    return 0;
}