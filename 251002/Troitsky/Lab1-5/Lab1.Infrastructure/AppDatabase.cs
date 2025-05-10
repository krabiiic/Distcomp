using Lab1.Infrastructure.Entities;
using Lab1.Core.Abstractions;
namespace Lab1.Infrastructure
{
    public class AppDatabase
    {
        public Storage<UserEntity> Users = new Storage<UserEntity>();
        public Storage<NewsEntity> News = new Storage<NewsEntity>();
        public Storage<TagEntity> Tags = new Storage<TagEntity>();
//        public Storage<PostEntity> Posts = new Storage<PostEntity>();
    }
}
