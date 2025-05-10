using System.Text;
using System.Text.Json;
using Confluent.Kafka;
namespace TaskRedis.ServiceDefaults.Kafka.Serialization;

public sealed class KafkaJsonDeserializer<T> : IAsyncDeserializer<T> where T : class
{
    public Task<T> DeserializeAsync(ReadOnlyMemory<byte> data, bool isNull, SerializationContext context)
    {
        if (isNull)
            return Task.FromResult<T>(null!);
        
        var json = Encoding.UTF8.GetString(data.Span);
        return Task.FromResult(JsonSerializer.Deserialize<T>(json))!;
    }
}
