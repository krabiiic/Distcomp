using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace publisher.ToDiscussion.DTO
{
    public record DiscussionNoteRequestTo(
        [property: JsonPropertyName("id")]
        long Id,
        [property: JsonPropertyName("aticleId")]
        long ArticleId,
        [property: JsonPropertyName("content")]
        [StringLength(2048, MinimumLength = 2)]
        string Content,
        [property: JsonPropertyName("country")]
        string Country
    )
    {
        public DiscussionNoteRequestTo(long id)
            : this(id, 0, string.Empty, string.Empty)
        {
        }
    }
}
