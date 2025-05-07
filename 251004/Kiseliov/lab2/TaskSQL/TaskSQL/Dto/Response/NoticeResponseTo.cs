using System.Text.Json.Serialization;

namespace TaskSQL.Dto.Response;

public record NoticeResponseTo(
    [property: JsonPropertyName("id")] long id,
    [property: JsonPropertyName("tweetId")]
    long Tweetid,
    [property: JsonPropertyName("content")]
    string Content
);