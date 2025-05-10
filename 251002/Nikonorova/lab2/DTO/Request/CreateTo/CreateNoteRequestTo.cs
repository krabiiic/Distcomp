using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace lab2_jpa.DTO.Request.CreateTo
{
    public record CreateNoteRequestTo(
    [property: JsonPropertyName("articleid")]
    long Articleid,
    [property: JsonPropertyName("content")]
    [StringLength(2048, MinimumLength = 2)]
    string Content
);
}
