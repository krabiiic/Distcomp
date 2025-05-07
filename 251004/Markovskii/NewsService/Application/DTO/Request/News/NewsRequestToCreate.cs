using Application.DTO.Request.Mark;

namespace Application.DTO.Request.News;

public class NewsRequestToCreate
{
    public long EditorId { get; init; }
    public string Title { get; init; }
    public string Content { get; init; }
    public List<string>? Marks { get; init; }
}