

namespace Core.Entities;

public class Message
{
    public long Id { get; init; }
    
    public long ArticleId { get; init; }
    
    public string Content { get; init; } = string.Empty;
}