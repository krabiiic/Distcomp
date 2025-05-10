using System.Numerics;
using Lab1.Core.Abstractions;
namespace Lab1.Infrastructure.Entities
{
    public class CreatorEntity : IEntity
    {
        public ulong Id { get; set; } = 0;
        public string Login { get; set; } = string.Empty;
        public string Password { get; set; } = string.Empty;
        public string FirstName { get; set; } = string.Empty;
        public string LastName { get; set; } = string.Empty;
        public IEnumerable<IssueEntity> Issues { get; set; } = Enumerable.Empty<IssueEntity>();
    }
}
