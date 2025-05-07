using System.Text.Json.Serialization;

namespace publisher.DTO.Response
{
    public record ArticleResponseTo(
    [property: JsonPropertyName("id")] long Id,
    [property: JsonPropertyName("creatorId")]
    long CreatorId,
    [property: JsonPropertyName("title")] string Title,
    [property: JsonPropertyName("content")]
    string Content
);
}
