using Confluent.Kafka;
using System;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using WebApplication1.DTO;
using WebApplication1.Repository;

namespace WebApplication1.Service
{
    public class KafkaNoteStatusUpdaterService : IDisposable
    {
        private readonly ConsumerConfig _consumerConfig;
        private readonly IServiceScopeFactory _serviceScopeFactory;

        public KafkaNoteStatusUpdaterService(IServiceScopeFactory serviceScopeFactory)
        {
            _serviceScopeFactory = serviceScopeFactory;
            _consumerConfig = new ConsumerConfig
            {
                BootstrapServers = "localhost:9092",
                GroupId = "publisher-group",
                AutoOffsetReset = AutoOffsetReset.Earliest,
                SecurityProtocol = SecurityProtocol.Plaintext
            };
        }

        public async Task RunAsync(CancellationToken cancellationToken)
        {
            using var consumer = new ConsumerBuilder<string, string>(_consumerConfig).Build();
            consumer.Subscribe("OutTopic");

            while (!cancellationToken.IsCancellationRequested)
            {
                try
                {
                    var consumeResult = consumer.Consume(cancellationToken);
                    var noteDto = JsonSerializer.Deserialize<NoteResponseTo>(consumeResult.Message.Value);
                    if (noteDto == null)
                        continue;

                    using (var scope = _serviceScopeFactory.CreateScope())
                    {
                        var repository = scope.ServiceProvider.GetRequiredService<IRepository<WebApplication1.Entity.Note>>();
                        var note = await repository.GetByIdAsync(noteDto.Id);
                        if (note != null)
                        {
                            Console.WriteLine($"Note {note.Id} обновлён: статус модерации {noteDto.State}");
                        }
                    }
                }
                catch (ConsumeException ex)
                {
                    Console.WriteLine($"Ошибка при потреблении сообщения: {ex.Error.Reason}");
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Непредвиденная ошибка: {ex.Message}");
                }
            }
            consumer.Close();
        }

        public void Dispose()
        {
        }
    }
}
