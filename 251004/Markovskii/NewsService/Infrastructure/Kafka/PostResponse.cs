namespace Infrastructure.Kafka;
public class PostResponse
{
    public string CorrelationId { get; set; }
    public object Result { get; set; }
}