using Confluent.Kafka;
using Core;
using Discussion.abstractions;
using Discussion.services;
using DTO.requests;
using System.Text.Json;

namespace Discussion.Kafka
{
    public class NoteConsumerService : BackgroundService
    {
        private readonly IConsumer<string, KafkaMessage> _consumer;
        private readonly IProducer<string, KafkaMessage> _producer;
        private readonly IServiceScopeFactory _scopeFactory;
        private string _outTopic = "OutTopic";
        private string _inTopic = "InTopic";

        public NoteConsumerService(IConfiguration configuration, IServiceScopeFactory scopeFactory)
        {
            _scopeFactory = scopeFactory;
            
            var config = new ConsumerConfig
            {
                BootstrapServers = configuration["Kafka:BootstrapServers"],
                GroupId = "discussion-consumer-group",
                AutoOffsetReset = AutoOffsetReset.Earliest, // читаем все (или earliest если точно нужно)
                EnableAutoCommit = true,                    // чтобы offset'ы сохранялись

                SessionTimeoutMs = 6000,         // минимум 6000
                MaxPollIntervalMs = 10000,       // должен быть >= SessionTimeoutMs

                FetchMinBytes = 1,
                FetchWaitMaxMs = 10,             // доставить как можно быстрее
                EnablePartitionEof = false
            };

            _consumer = new ConsumerBuilder<string, KafkaMessage>(config)
                .SetKeyDeserializer(Deserializers.Utf8)
                .SetValueDeserializer(new NoteMessageJsonDeserializer())
                .Build();

            var producerConfig = new ProducerConfig
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

            _producer = new ProducerBuilder<string, KafkaMessage>(producerConfig)
                .SetKeySerializer(Serializers.Utf8)
                .SetValueSerializer(new NoteMessageJsonSerializer())
                .Build();
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            _consumer.Subscribe("InTopic");

            Console.WriteLine($" -/-/-/-/-/- Discussion start working");

            while (!stoppingToken.IsCancellationRequested)
            {
                try
                {
                    using var scope = _scopeFactory.CreateScope();
                    var noteService = scope.ServiceProvider.GetRequiredService<INoteService>();

                    var result = _consumer.Consume(stoppingToken);
                    var noteMessage = result.Message.Value;

                    long id;

                    switch (noteMessage.Action)
                    {
                        case NoteAction.Get:
                            Console.WriteLine($" -/-/-/-/-/- Discussion GET receive  at {DateTime.Now:HH:mm:ss.fff}");
                            id = noteMessage.RequestId;
                            var res = await noteService.GetByIdAsync(id);
                            if (res == null)
                                noteMessage.State = NoteState.DECLINE;
                            else
                            {
                                noteMessage.State = NoteState.APPROVE;
                                noteMessage.Note = new Note { Id = res.Id, StoryId = res.StoryId, Content = res.Content };
                            }
                            break;

                        case NoteAction.GetAll:
                            Console.WriteLine($" -/-/-/-/-/- Discussion GET ALL receive  at {DateTime.Now:HH:mm:ss.fff}");
                            var notes = await noteService.GetAllAsync();
                            if (notes == null)
                                noteMessage.State = NoteState.DECLINE;
                            else
                            {
                                noteMessage.State = NoteState.APPROVE;
                                noteMessage.Notes = notes.Select(n => new Note
                                {
                                    Id = n.Id,
                                    StoryId = n.StoryId,
                                    Content = n.Content
                                }).ToList();
                            }
                            break;

                        case NoteAction.Create:
                            Console.WriteLine($" -/-/-/-/-/- Discussion POST receive  at {DateTime.Now:HH:mm:ss.fff}");
                            var created = await noteService.AddNoteAsync(noteMessage.Note);
                            noteMessage.State = created != null ? NoteState.APPROVE : NoteState.DECLINE;
                            break;

                        case NoteAction.Update:
                            Console.WriteLine($" -/-/-/-/-/- Discussion UPDATE receive  at {DateTime.Now:HH:mm:ss.fff}");
                            id = noteMessage.Note.Id;
                            var isUpdate = await noteService.UpdateAsync(id, new NoteRequestTo
                            {
                                Content = noteMessage.Note.Content,
                                StoryId = noteMessage.Note.StoryId
                            });
                            noteMessage.State = isUpdate ? NoteState.APPROVE : NoteState.DECLINE;
                            break;

                        case NoteAction.Delete:
                            Console.WriteLine($" -/-/-/-/-/- Discussion DELETE receive  at {DateTime.Now:HH:mm:ss.fff}");
                            id = noteMessage.Note.Id;
                            var isDelete = await noteService.DeleteNoteAsync(id);
                            noteMessage.State = isDelete ? NoteState.APPROVE : NoteState.DECLINE;
                            break;
                    }

                    await _producer.ProduceAsync("OutTopic", new Message<string, KafkaMessage>
                    {
                        Key = noteMessage.RequestId.ToString(),
                        Value = noteMessage
                    }, stoppingToken);
                    _producer.Flush(TimeSpan.FromSeconds(1)); // важно!

                    Console.WriteLine($" -/-/-/-/-/- Discussion id = {noteMessage.RequestId} responce sent at {DateTime.Now:HH:mm:ss.fff}");
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Error in Kafka moderation consumer: {ex.Message}");
                }
            }

            _consumer.Close();
        }
    }

    // Десериализатор Note
    public class NoteMessageJsonDeserializer : IDeserializer<KafkaMessage>
    {
        public KafkaMessage Deserialize(ReadOnlySpan<byte> data, bool isNull, SerializationContext context)
        {
            return JsonSerializer.Deserialize<KafkaMessage>(data)!;
        }
    }

    // Сериализатор Note
    public class NoteMessageJsonSerializer : ISerializer<KafkaMessage>
    {
        public byte[] Serialize(KafkaMessage data, SerializationContext context)
        {
            return JsonSerializer.SerializeToUtf8Bytes(data);
        }
    }
}
