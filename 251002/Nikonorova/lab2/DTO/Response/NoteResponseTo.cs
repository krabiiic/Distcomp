using System.Text.Json.Serialization;

namespace lab2_jpa.DTO.Response
{
    public record NoteResponseTo(
    [property: JsonPropertyName("id")] long id,
    [property: JsonPropertyName("articleId")]
    long Articleid,
    [property: JsonPropertyName("content")]
    string Content
);
}
