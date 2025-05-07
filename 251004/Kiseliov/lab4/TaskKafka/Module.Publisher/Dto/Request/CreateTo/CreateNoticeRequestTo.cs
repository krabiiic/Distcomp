using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;
namespace Publisher.Dto.Request.CreateTo;

public record CreateNoticeRequestTo(
    [property: JsonPropertyName("tweetId")]
    long TweetId,
    [property: JsonPropertyName("content")]
    [StringLength(2048, MinimumLength = 2)]
    string Content
);