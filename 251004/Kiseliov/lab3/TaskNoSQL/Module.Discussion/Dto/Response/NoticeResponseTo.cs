using System.Text.Json.Serialization;
namespace Discussion.Dto.Response;

public record NoticeResponseTo(
    [property: JsonPropertyName("id")] 
    long Id,
    [property: JsonPropertyName("tweetId")]
    long TweetId,
    [property: JsonPropertyName("content")]
    string Content
);