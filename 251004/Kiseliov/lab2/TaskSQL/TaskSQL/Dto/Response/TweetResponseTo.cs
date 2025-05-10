using System.Text.Json.Serialization;

namespace TaskSQL.Dto.Response;

public record TweetResponseTo(
    [property: JsonPropertyName("id")] long id,
    [property: JsonPropertyName("creatorId")]
    long creator_id,
    [property: JsonPropertyName("title")] string Title,
    [property: JsonPropertyName("content")]
    string Content
);