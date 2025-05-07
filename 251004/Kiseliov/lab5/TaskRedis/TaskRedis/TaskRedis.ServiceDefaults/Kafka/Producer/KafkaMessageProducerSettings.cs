using Confluent.Kafka;
using TaskRedis.ServiceDefaults.Kafka.Serialization;
namespace TaskRedis.ServiceDefaults.Kafka.Producer;

public class KafkaMessageProducerSettings<TK, TV>
{
    public string Topic { get; set; } = string.Empty;
    public IAsyncSerializer<TK> KeySerializer { get; set; } = new KafkaJsonSerializer<TK>();
    public IAsyncSerializer<TV> ValueSerializer { get; set; } = new KafkaJsonSerializer<TV>();
}
