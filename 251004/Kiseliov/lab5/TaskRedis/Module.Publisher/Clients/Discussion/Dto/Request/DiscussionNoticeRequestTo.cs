using System.Text.Json.Serialization;
namespace Publisher.Clients.Discussion.Dto.Request;

public record DiscussionNoticeRequestTo(
    [property: JsonPropertyName("id")] 
    long Id,
    [property: JsonPropertyName("tweetId")]
    long TweetId,
    [property: JsonPropertyName("content")]
    string Content,
    [property: JsonPropertyName("country")]
    string Country
)
{
    public DiscussionNoticeRequestTo(long id) : this(id, 0, string.Empty, string.Empty)
    {}

    public DiscussionNoticeRequestTo() : this(0, 0, string.Empty, string.Empty)
    {}
}
