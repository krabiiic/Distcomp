using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace lab2_jpa.DTO.Request.UpdateTo
{
    public record UpdateArticleRequestTo(
    [property: JsonPropertyName("id")] long id,
    [property: JsonPropertyName("creatorId")]
    long creator_id,
    [property: JsonPropertyName("title")]
    [StringLength(64, MinimumLength = 2)]
    string Title,
    [property: JsonPropertyName("content")]
    [StringLength(2048, MinimumLength = 4)]
    string Content
);
}
