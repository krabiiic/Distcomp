using System.Numerics;
using Lab1.Core.Abstractions;
namespace Lab1.Infrastructure.Entities
{
    public class StickerEntity : IEntity
    {
        public ulong Id { get; set; } = 0;
        public string Name { get; set; } = string.Empty;
        public IEnumerable<IssueStickerEntity> IssueStickers { get; set; } = Enumerable.Empty<IssueStickerEntity>();
    }
}
