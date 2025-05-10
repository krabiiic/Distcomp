#include "Server.hpp"
#include "Editor.hpp"
#include "Article.hpp"
#include "Comment.hpp"
#include "Label.hpp"


int main() {
    Server<Editor, Article, Comment, Label> server;
    server.start_server();
    return 0;
}