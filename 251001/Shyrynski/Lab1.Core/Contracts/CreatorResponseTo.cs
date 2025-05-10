using System.Numerics;
namespace Lab1.Core.Contracts
{
    public record CreatorResponseTo
    {
        public long id { get; set; } = 0;
        public string login { get; set; } = string.Empty;
        public string password { get; set; } = string.Empty;
        public string firstname { get; set; } = string.Empty;
        public string lastname { get; set; } = string.Empty;
    }
}
