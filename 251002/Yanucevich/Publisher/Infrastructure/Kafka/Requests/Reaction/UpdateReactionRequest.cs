namespace Infrastructure.Kafka.Requests;

public class UpdateReactionRequest
{
    public long Id { get; init; }
    public long TweetId { get; init; }
    public string Content { get; init; }
}
