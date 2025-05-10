using Confluent.Kafka;
using System.Text.Json;
using Lab1.Core.Abstractions;
public class KafkaProducer : IProducer
{
    private readonly IProducer<string, string> _producer; // Инициализируем сразу
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

    public async Task SendMessageAsync(string issueId, object message)
    {
        var messageString = JsonSerializer.Serialize(message, _options);
        await _producer.ProduceAsync(Topic, new Message<string, string>
        {
            Key = issueId,
            Value = messageString
        });
    }
}