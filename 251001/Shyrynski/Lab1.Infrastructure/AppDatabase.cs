using Lab1.Infrastructure.Entities;
using Lab1.Core.Abstractions;
namespace Lab1.Infrastructure
{
    public class AppDatabase
    {
        public Storage<CreatorEntity> Creators = new Storage<CreatorEntity>();
        public Storage<IssueEntity> Issues = new Storage<IssueEntity>();
        public Storage<StickerEntity> Stickers = new Storage<StickerEntity>();
    }
}
