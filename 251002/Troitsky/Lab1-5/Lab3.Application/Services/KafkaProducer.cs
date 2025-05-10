using AutoMapper;
using Confluent.Kafka;
using Lab3.Core.Abstractions;
using System.Text.Json;

namespace Lab3.Application.Services
{
    public class KafkaProducer : IProducer
    {
        // Отложенная инициализация продюсера, ресурсы создаются только при первом вызове
        private readonly Lazy<IProducer<string, string>> _producer;
        private const string Topic = "OutTopic";

        public KafkaProducer()
        {
            _producer = new Lazy<IProducer<string, string>>(() =>
            {
                var config = new ProducerConfig
                {
                    BootstrapServers = "kafka:9092",
                    Acks = Acks.Leader
                };
                return new ProducerBuilder<string, string>(config).Build();
            });
        }


        public async Task SendMessageAsync(string newsId, object post)
        {
            var postString = JsonSerializer.Serialize(post);
            await _producer.Value.ProduceAsync(Topic, new Message<string, string>
            {
                Key = newsId,
                Value = postString
            });
        }
    }
}
