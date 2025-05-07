using Confluent.Kafka;
using MyApp.Models;
using System.Text.Json;

namespace MyApp.Services
{

    public class KafkaNoteProducer
    {
        private readonly IProducer<string, string> _producer;
        private const string Topic = "InTopic";
        public KafkaNoteProducer(IConfiguration config)
        {
            var bootstrapServers = config["Kafka:BootstrapServers"];
            var producerConfig = new ProducerConfig { BootstrapServers = bootstrapServers };
            _producer = new ProducerBuilder<string, string>(producerConfig).Build();
        }

        public async Task SendNoteAsync(Note note)
        {
            var key = note.storyId.ToString();
            var value = JsonSerializer.Serialize(note);
            await _producer.ProduceAsync(Topic, new Message<string, string> { Key = key, Value = value });
        }
    }
}
