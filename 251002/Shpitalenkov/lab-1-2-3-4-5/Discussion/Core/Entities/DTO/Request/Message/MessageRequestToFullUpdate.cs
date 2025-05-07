namespace Core.DTO;

public class MessageRequestToFullUpdate
{
    public long Id { get; init; }
    public long ArticleId { get; init; }
    public string Content { get; init; }
}