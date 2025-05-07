using System.Text.Json.Serialization;

namespace publisher.ToDiscussion.DTO
{
    public record DiscussionNoteResponseTo(
    [property: JsonPropertyName("id")]
    long Id,
    [property: JsonPropertyName("articleId")]
    long ArticleId,
    [property: JsonPropertyName("content")]
    string Content
);
}
