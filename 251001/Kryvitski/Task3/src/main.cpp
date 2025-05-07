#include "PublisherServer.hpp"
#include "DiscussionServer.hpp"
#include "Editor.hpp"
#include "Article.hpp"
#include "Comment.hpp"
#include "Label.hpp"
#include <thread>

int main() {
    PublisherServer<Editor, Article, Label> publisher;
    DiscussionServer<Comment> discussion;

    auto discussion_thread = std::thread([&](){
        discussion.start_server();
    });
    
    publisher.redirect("comments", 24130);
    publisher.start_server();
    discussion_thread.join();
    return 0;
}