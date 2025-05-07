using Confluent.Kafka;
using TaskKafka.ServiceDefaults.Kafka.Serialization;
namespace TaskKafka.ServiceDefaults.Kafka.Consumer;

public class KafkaHandlerSettings<TK, TV> 
    where TK : class 
    where TV : class
{
    public string Topic { get; set; } = string.Empty;
    public IAsyncDeserializer<TK> KeyDeserializer { get; set; } = new KafkaJsonDeserializer<TK>();
    public IAsyncDeserializer<TV> ValueDeserializer { get; set; } = new KafkaJsonDeserializer<TV>();
    
    internal ConsumerConfig ConsumerConfig { get; set; } = new();
}
