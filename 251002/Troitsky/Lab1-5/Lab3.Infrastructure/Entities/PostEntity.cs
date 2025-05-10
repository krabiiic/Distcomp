using System.Numerics;
using Lab3.Core.Abstractions;
namespace Lab3.Infrastructure.Entities
{
    public class PostEntity : IEntity
    {
        public ulong Id { get; set; } = 0;
        public ulong NewsId { get; set; } = 0;
        public string Country {  get; set; } = string.Empty;
        public string Content { get; set; } = string.Empty;
    }
}
