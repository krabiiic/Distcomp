namespace TaskKafka.ServiceDefaults.Kafka.Consumer;

public interface IKafkaHandler<in TK, in TV>
{
    Task HandleAsync(TK key, TV value);
}
