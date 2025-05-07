

namespace Domain.Entities;

public class Post
{
    public long Id { get; init; }
    
    public long NewsId { get; init; }
    
    public string Content { get; init; } = string.Empty;
}