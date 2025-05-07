using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;
namespace Publisher.Dto.Request.UpdateTo;

public record UpdateNoticeRequestTo(
    [property: JsonPropertyName("id")] 
    long Id,
    [property: JsonPropertyName("tweetId")]
    long TweetId,
    [property: JsonPropertyName("content")]
    [StringLength(2048, MinimumLength = 2)]
    string Content
);