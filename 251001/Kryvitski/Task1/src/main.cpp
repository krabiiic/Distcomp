#include "Server.hpp"
#include "Article.hpp"
#include "Comment.hpp"
#include "Editor.hpp"
#include "Label.hpp"

int main() {
    Server<Label, Article, Comment, Editor> server;
    server.start_server();
    return 0;
}