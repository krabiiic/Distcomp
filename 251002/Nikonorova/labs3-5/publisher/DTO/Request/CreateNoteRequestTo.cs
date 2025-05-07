using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace publisher.DTO.Request
{

    public record CreateNoteRequestTo(

        [property: JsonPropertyName("articleId")]
        long ArticleId,
        [property: JsonPropertyName("content")]
        [StringLength(2048, MinimumLength = 2)]
        string Content
    );
}
