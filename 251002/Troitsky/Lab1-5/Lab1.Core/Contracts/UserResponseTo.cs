using System.Numerics;
namespace Lab1.Core.Contracts
{
    // DTO определяют структуру данных, возвращаемую клиентам
    public record UserResponseTo 
    {
        public long id { get; set; } = 0;
        public string login { get; set; } = string.Empty;
        public string password { get; set; } = string.Empty;
        public string firstname { get; set; } = string.Empty;
        public string lastname { get; set; } = string.Empty;
    }
}
