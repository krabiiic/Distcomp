namespace Core.DTO;

public class MessageRequestToCreate
{
    public long Id { get; init; }
    public long ArticleId { get; init; }
    public string Content { get; init; }
}