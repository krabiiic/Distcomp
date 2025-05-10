using Confluent.Kafka;
using System.Text.Json;
using Lab1.Core.Abstractions;
public class KafkaProducer : IProducer
{
    private readonly IProducer<string, string> _producer; 
    private const string Topic = "InTopic";
    private readonly JsonSerializerOptions _options = new() { WriteIndented = true };

    public KafkaProducer()
    {
        var config = new ProducerConfig
        {
            BootstrapServers = "kafka:9092",
            Acks = Acks.Leader
        };
        _producer = new ProducerBuilder<string, string>(config).Build();
    }

    public async Task SendMessageAsync(string newsId, object post)
    {
        var postString = JsonSerializer.Serialize(post, _options);
        await _producer.ProduceAsync(Topic, new Message<string, string>
        {
            Key = newsId, 
            Value = postString
        });
    }
}