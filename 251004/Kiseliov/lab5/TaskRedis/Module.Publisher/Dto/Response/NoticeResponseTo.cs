using System.Text.Json.Serialization;
namespace Publisher.Dto.Response;

public record NoticeResponseTo(
    [property: JsonPropertyName("id")] 
    long Id,
    [property: JsonPropertyName("tweetId")]
    long TweetId,
    [property: JsonPropertyName("content")]
    string Content
);