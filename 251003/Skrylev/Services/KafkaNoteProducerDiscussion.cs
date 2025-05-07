using Confluent.Kafka;
using Microsoft.Extensions.Configuration;
using MyApp.Models;
using System.Text.Json;

namespace MyApp.Services
{
    public class KafkaNoteConsumerDiscussion
    {
        private readonly IConsumer<string, string> _consumer;
        private const string Topic = "InTopic";
        public KafkaNoteConsumerDiscussion(IConfiguration config)
        {
            var bootstrapServers = config["Kafka:BootstrapServers"];
            var groupId = config["Kafka:DiscussionGroupId"];
            var consumerConfig = new ConsumerConfig
            {
                BootstrapServers = bootstrapServers,
                GroupId = groupId,
                AutoOffsetReset = AutoOffsetReset.Earliest
            };
            _consumer = new ConsumerBuilder<string, string>(consumerConfig).Build();
        }
        public void Listen(Action<Note> onNoteReceived)
        {
            _consumer.Subscribe(Topic);
            while (true)
            {
                var result = _consumer.Consume();
                var note = JsonSerializer.Deserialize<Note>(result.Message.Value);
                onNoteReceived(note);
            }
        }
    }
}