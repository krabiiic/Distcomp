using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace lab2_jpa.DTO.Request.CreateTo
{
    public record CreateArticleRequestTo(
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
