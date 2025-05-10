using Confluent.Kafka;
using Discussion.DTO;
using Discussion.Models;
using Discussion.Services;
using System;
using System.Linq;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;

namespace Discussion
{
    public class KafkaNoteConsumerService : IDisposable
    {
        private readonly ConsumerConfig _consumerConfig;
        private readonly ProducerConfig _producerConfig;
        private readonly INoteService _noteService;
        private readonly IProducer<string, string> _producer;

        public KafkaNoteConsumerService(INoteService noteService)
        {
            _noteService = noteService;
            _consumerConfig = new ConsumerConfig
            {
                BootstrapServers = "localhost:9092",
                GroupId = "discussion-group",
                AutoOffsetReset = AutoOffsetReset.Earliest,
                SecurityProtocol = SecurityProtocol.Plaintext
            };
            _producerConfig = new ProducerConfig
            {
                BootstrapServers = "localhost:9092",
                SecurityProtocol = SecurityProtocol.Plaintext
            };

            _producer = new ProducerBuilder<string, string>(_producerConfig).Build();
        }

        public async Task RunAsync(CancellationToken stoppingToken)
        {
            using var consumer = new ConsumerBuilder<string, string>(_consumerConfig).Build();

            try
            {
                consumer.Subscribe("InTopic");
                Console.WriteLine("Consumer успешно подписался на InTopic.");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Ошибка подписки на InTopic: {ex.Message}");
                return;
            }

            while (!stoppingToken.IsCancellationRequested)
            {
                try
                {
                    var consumeResult = consumer.Consume(TimeSpan.FromSeconds(1));
                    if (consumeResult == null)
                        continue;

                    var noteDto = JsonSerializer.Deserialize<NoteResponseTo>(consumeResult.Message.Value);
                    if (noteDto == null)
                        continue;

                    noteDto.State = ModerationAlgorithm(noteDto.Content);

                    var existingNote = await _noteService.GetNoteByIdAsync(noteDto.Id);
                    if (existingNote == null)
                    {
                        var newNote = new Note
                        {
                            Id = noteDto.Id,
                            Country = noteDto.Country,
                            ArticleId = noteDto.ArticleId,
                            Content = noteDto.Content,
                            Modified = DateTime.UtcNow
                        };
                        await _noteService.CreateNoteAsync(newNote);
                    }
                    else
                    {
                        existingNote.Content = noteDto.Content;
                        existingNote.Modified = DateTime.UtcNow;
                        await _noteService.UpdateNoteAsync(noteDto.Id, existingNote);
                    }

                    var messageValue = JsonSerializer.Serialize(noteDto);
                    await _producer.ProduceAsync("OutTopic", new Message<string, string>
                    {
                        Key = noteDto.ArticleId.ToString(),
                        Value = messageValue
                    }, stoppingToken);

                    Console.WriteLine($"Обработано сообщение для заметки {noteDto.Id}");
                }
                catch (ConsumeException ex)
                {
                    Console.WriteLine($"Ошибка потребления сообщения: {ex.Error.Reason}");
                    await Task.Delay(2000, stoppingToken);
                }
                catch (ProduceException<string, string> ex)
                {
                    Console.WriteLine($"Ошибка отправки сообщения: {ex.Error.Reason}");
                    await Task.Delay(2000, stoppingToken);
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Непредвиденная ошибка во внутреннем цикле: {ex.Message}");
                    await Task.Delay(2000, stoppingToken);
                }
            }

            consumer.Close();
        }

        private NoteState ModerationAlgorithm(string content)
        {
            string[] stopWords = { "badword1", "badword2" };
            return stopWords.Any(word => content.Contains(word, StringComparison.OrdinalIgnoreCase))
                ? NoteState.DECLINE
                : NoteState.APPROVE;
        }

        public void Dispose()
        {
            _producer?.Dispose();
        }
    }
}
