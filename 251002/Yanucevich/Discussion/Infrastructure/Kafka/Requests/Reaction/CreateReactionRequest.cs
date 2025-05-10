namespace Infrastructure.Kafka.Requests;

public class CreateReactionRequest
{
    public long Id { get; init; }
    public long TweetId { get; init; }
    public string Content { get; init; }
}
