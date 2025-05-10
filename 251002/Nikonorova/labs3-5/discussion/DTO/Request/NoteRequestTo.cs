using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace discussion.DTO.Request
{
    public record NoteRequestTo (

        [property: JsonPropertyName("id")]
        long Id,
        [property: JsonPropertyName("articleId")]
        long ArticleId,
        [property: JsonPropertyName("content")]
        [StringLength(2048, MinimumLength = 2)]
        string Content,
        [property: JsonPropertyName("country")]
        [StringLength(2, MinimumLength = 2)]
        string Country


    );
           
    
}
