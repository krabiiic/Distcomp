using Confluent.Kafka;
using MyApp.Models;
using System.Text.Json;

namespace MyApp.Services
{
    public class KafkaNoteStatusConsumer
    {
        private readonly IConsumer<string, string> _consumer;
        private const string Topic = "OutTopic";
        public KafkaNoteStatusConsumer(IConfiguration config)
        {
            var bootstrapServers = config["Kafka:BootstrapServers"];
            var groupId = config["Kafka:PublisherGroupId"];
            var consumerConfig = new ConsumerConfig
            {
                BootstrapServers = bootstrapServers,
                GroupId = groupId,
                AutoOffsetReset = AutoOffsetReset.Earliest
            };
            _consumer = new ConsumerBuilder<string, string>(consumerConfig).Build();
        }
        public void Listen(Action<Note> onNoteStatus)
        {
            _consumer.Subscribe(Topic);
            while (true)
            {
                var result = _consumer.Consume();
                var note = JsonSerializer.Deserialize<Note>(result.Message.Value);
                onNoteStatus(note);
            }
        }
    }
}
