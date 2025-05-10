using System.Numerics;
using Lab1.Core.Abstractions;
namespace Lab1.Infrastructure.Entities
{
    public class NewsEntity : IEntity
    {
        public ulong Id { get; set; } = 0;
        public ulong UserId { get; set; } = 0;
        public string Title { get; set; } = string.Empty;
        public string Content { get; set; } = string.Empty;
        public DateTime Created { get; set; } = DateTime.MinValue;
        public DateTime Modified { get; set; } = DateTime.MinValue;
        public UserEntity? User { get; set; } 
        public IEnumerable<NewsTagEntity> NewsTags { get; set; } = Enumerable.Empty<NewsTagEntity>();
//        public IEnumerable<PostEntity> Posts { get; set; } = Enumerable.Empty<PostEntity>();

    }
}
