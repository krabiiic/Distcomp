using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;
namespace Publisher.Clients.Discussion.Dto.Request;

public record DiscussionNoticeRequestTo(
    [property: JsonPropertyName("id")] 
    long Id,
    [property: JsonPropertyName("tweetId")]
    long TweetId,
    [property: JsonPropertyName("content")]
    [StringLength(2048, MinimumLength = 2)]
    string Content,
    [property: JsonPropertyName("country")]
    string Country
);