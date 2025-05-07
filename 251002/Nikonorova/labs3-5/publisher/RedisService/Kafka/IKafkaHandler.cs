namespace publisher.RedisService.Kafka
{
    public interface IKafkaHandler<in TK, in TV>
    {
        Task HandleAsync(TK key, TV value);
    }
}
