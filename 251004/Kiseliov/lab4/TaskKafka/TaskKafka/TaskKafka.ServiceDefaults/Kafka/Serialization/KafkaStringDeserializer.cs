using System.Text;
using Confluent.Kafka;
namespace TaskKafka.ServiceDefaults.Kafka.Serialization;

public sealed class KafkaStringDeserializer : IAsyncDeserializer<string>
{
    public Task<string> DeserializeAsync(ReadOnlyMemory<byte> data, bool isNull, SerializationContext context) => 
        Task.FromResult(isNull ? string.Empty : Encoding.UTF8.GetString(data.Span));
}
