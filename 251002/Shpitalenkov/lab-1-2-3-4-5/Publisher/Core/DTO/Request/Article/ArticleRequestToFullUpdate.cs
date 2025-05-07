namespace Core.DTO;

public class ArticleRequestToFullUpdate
{
    public long Id { get; init; }
    public long CreatorId { get; init; }
    public string Title { get; init; }
    public string Content { get; init; }
}