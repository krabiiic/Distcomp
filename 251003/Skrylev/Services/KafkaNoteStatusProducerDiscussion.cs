using Confluent.Kafka;
using Microsoft.Extensions.Configuration;
using MyApp.Models;
using System.Text.Json;

namespace MyApp.Services
{
    public class KafkaNoteStatusProducerDiscussion
    {
        private readonly IProducer<string, string> _producer;
        private const string Topic = "OutTopic";
        public KafkaNoteStatusProducerDiscussion(IConfiguration config)
        {
            var bootstrapServers = config["Kafka:BootstrapServers"];
            var producerConfig = new ProducerConfig { BootstrapServers = bootstrapServers };
            _producer = new ProducerBuilder<string, string>(producerConfig).Build();
        }
        public async Task SendStatusAsync(Note note)
        {
            var key = note.storyId.ToString();
            var value = JsonSerializer.Serialize(note);
            await _producer.ProduceAsync(Topic, new Message<string, string> { Key = key, Value = value });
        }
    }
}