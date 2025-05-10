using Confluent.Kafka;
using Microsoft.Extensions.Options;
namespace TaskKafka.ServiceDefaults.Kafka.Producer;

public sealed class KafkaMessageProducer<TK, TV>(
    IProducer<TK, TV> producer,
    IOptions<KafkaMessageProducerSettings<TK, TV>> options) : IDisposable
    where TK : class
    where TV : class
{
    private readonly KafkaMessageProducerSettings<TK, TV> _settings = options.Value;
    public string Id = Guid.NewGuid().ToString();
    
    public async Task ProduceAsync(TK key, TV value)
    {
        if (string.IsNullOrEmpty(Id))
            return;
        
        await producer.ProduceAsync(_settings.Topic, new Message<TK, TV>
        {
            Key = key,
            Value = value
        });
    }
    
    public void Dispose() => producer.Dispose();
}
