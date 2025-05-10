using System.Text;
using Confluent.Kafka;
namespace TaskKafka.ServiceDefaults.Kafka.Serialization;

public sealed class KafkaStringSerializer : IAsyncSerializer<string>
{
    public Task<byte[]> SerializeAsync(string data, SerializationContext context) => 
        Task.FromResult(Encoding.UTF8.GetBytes(data));
}
