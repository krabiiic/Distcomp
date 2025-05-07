using System.Text.Json.Serialization;

namespace publisher.DTO.Response
{
    public record NoteResponseTo(
    [property: JsonPropertyName("id")]
    long Id,
    [property: JsonPropertyName("articleId")]
    long ArticleId,
    [property: JsonPropertyName("content")]
    string Content
);
}
