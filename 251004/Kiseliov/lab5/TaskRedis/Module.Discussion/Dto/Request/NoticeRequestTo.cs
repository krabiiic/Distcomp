using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;
namespace Discussion.Dto.Request;

public record NoticeRequestTo(
    [property: JsonPropertyName("id")]
    long Id,
    [property: JsonPropertyName("tweetId")]
    long TweetId,
    [property: JsonPropertyName("content")]
    [StringLength(2048, MinimumLength = 2)]
    string Content,
    [property: JsonPropertyName("country")]
    [StringLength(2, MinimumLength = 2)]
    string? Country
);