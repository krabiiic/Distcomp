namespace Core.DTO;

public class ArticleRequestToCreate
{
    public long CreatorId { get; init; }
    public string Title { get; init; }
    public string Content { get; init; }
    public List<string>? Tags { get; init; }
}