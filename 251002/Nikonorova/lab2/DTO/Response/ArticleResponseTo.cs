using System.Text.Json.Serialization;

namespace lab2_jpa.DTO.Response
{
    public record ArticleResponseTo(
    [property: JsonPropertyName("id")] long id,
    [property: JsonPropertyName("creatorId")]
    long creator_id,
    [property: JsonPropertyName("title")] string Title,
    [property: JsonPropertyName("content")]
    string Content
);
}
