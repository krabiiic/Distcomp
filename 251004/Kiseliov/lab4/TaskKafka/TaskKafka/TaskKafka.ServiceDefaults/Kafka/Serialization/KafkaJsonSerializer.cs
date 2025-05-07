using System.Text;
using System.Text.Json;
using Confluent.Kafka;
namespace TaskKafka.ServiceDefaults.Kafka.Serialization;

public sealed class KafkaJsonSerializer<T> : IAsyncSerializer<T>
{
    public Task<byte[]> SerializeAsync(T data, SerializationContext context)
    {
        var json = JsonSerializer.Serialize(data);
        return Task.FromResult(Encoding.UTF8.GetBytes(json));
    }
}
