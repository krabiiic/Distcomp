using Confluent.Kafka;
using TaskRedis.ServiceDefaults.Kafka.Serialization;
namespace TaskRedis.ServiceDefaults.Kafka.SyncClient;

public class KafkaSyncClientSetting<TInput, TOutput>
    where TInput : class
    where TOutput : class
{
    public string InTopic { get; set; } = string.Empty;
    public string OutTopic { get; set; } = string.Empty;
    public TimeSpan Timeout { get; set; } = TimeSpan.FromSeconds(1);
    public IAsyncSerializer<TInput> ValueSerializer { get; set; } = new KafkaJsonSerializer<TInput>();
    public IAsyncDeserializer<TOutput> ValueDeserializer { get; set; } = new KafkaJsonDeserializer<TOutput>();
}
