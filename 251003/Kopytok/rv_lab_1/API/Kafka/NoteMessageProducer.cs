using Confluent.Kafka;
using System.Text.Json;
using Core;

namespace API.Kafka
{
    public class NoteMessageProducer
    {
        private readonly IProducer<string, KafkaMessage> _producer;

        public NoteMessageProducer(IConfiguration configuration)
        {
            var config = new ProducerConfig
            {
                BootstrapServers = configuration["Kafka:BootstrapServers"],

                // 💥 Отправка без буферизации
                LingerMs = 0,               // Отправить сразу, не ждать накопления
                BatchSize = 1,              // Минимальный размер пакета
                Acks = Acks.All,            // Надежная доставка
                EnableIdempotence = true,  // Повторная отправка при сбое

                // (опционально) меньше памяти под буфер
                QueueBufferingMaxKbytes = 128,
                QueueBufferingMaxMessages = 1000
            };

            _producer = new ProducerBuilder<string, KafkaMessage>(config)
                .SetKeySerializer(Serializers.Utf8)  // Сериализация ключа как строки
                .SetValueSerializer(new NoteMessageJsonSerializer()) // Сериализатор для объектов NoteMessage
                .Build();
        }

        public async Task SendMessageAsync(KafkaMessage noteMessage)
        {
            var result = await _producer.ProduceAsync("InTopic", new Message<string, KafkaMessage>
            {
                Key = noteMessage.RequestId.ToString(),
                Value = noteMessage
            });
        }
    }
    public class NoteMessageJsonSerializer : ISerializer<KafkaMessage>
    {
        public byte[] Serialize(KafkaMessage data, SerializationContext context)
        {
            return JsonSerializer.SerializeToUtf8Bytes(data);
        }
    }
    public class NoteMessageJsonDeserializer : IDeserializer<KafkaMessage>
    {
        public KafkaMessage Deserialize(ReadOnlySpan<byte> data, bool isNull, SerializationContext context)
        {
            if (isNull) return null;
            return JsonSerializer.Deserialize<KafkaMessage>(data);
        }
    }
}
