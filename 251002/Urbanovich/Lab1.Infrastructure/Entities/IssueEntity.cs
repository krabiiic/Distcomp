using System.Numerics;
using Lab1.Core.Abstractions;
namespace Lab1.Infrastructure.Entities
{
    public class IssueEntity : IEntity
    {
        public ulong Id { get; set; } = 0;
        public ulong CreatorId { get; set; } = 0;
        public string Title { get; set; } = string.Empty;
        public string Content { get; set; } = string.Empty;
        public DateTime Created { get; set; } = DateTime.MinValue;
        public DateTime Modified { get; set; } = DateTime.MinValue;
        public CreatorEntity? Creator { get; set; }
        public IEnumerable<IssueStickerEntity> IssueStickers { get; set; } = Enumerable.Empty<IssueStickerEntity>();
    }
}
