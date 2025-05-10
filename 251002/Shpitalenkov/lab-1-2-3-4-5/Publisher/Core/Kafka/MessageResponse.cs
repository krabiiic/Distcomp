namespace Core.Kafka;
public class MessageResponse
{
    public string CorrelationId { get; set; }
    public object Result { get; set; }
}