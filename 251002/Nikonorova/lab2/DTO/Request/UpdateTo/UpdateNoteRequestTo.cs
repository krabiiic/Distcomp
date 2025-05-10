using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace lab2_jpa.DTO.Request.UpdateTo
{
    public record UpdateNoteRequestTo(
    [property: JsonPropertyName("id")] long id,
    [property: JsonPropertyName("articleid")]
    long Articleid,
    [property: JsonPropertyName("content")]
    [StringLength(2048, MinimumLength = 2)]
    string Content
);
}
