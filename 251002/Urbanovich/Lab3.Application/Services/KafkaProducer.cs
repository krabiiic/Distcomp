using AutoMapper;
using Confluent.Kafka;
using Lab3.Core.Abstractions;
using System.Text.Json;

namespace Lab3.Application.Services
{
    public class KafkaProducer : IProducer
    {
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


        public async Task SendMessageAsync(string issueId, object message)
        {
            var messageString = JsonSerializer.Serialize(message);
            await _producer.Value.ProduceAsync(Topic, new Message<string, string>
            {
                Key = issueId,
                Value = messageString
            });
            Console.WriteLine($"[KafkaProducer] Отправка в Kafka: {Topic}, message: {messageString}");
        }
    }
}
