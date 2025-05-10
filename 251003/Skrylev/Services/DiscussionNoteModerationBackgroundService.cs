namespace MyApp.Services
{
    using Microsoft.Extensions.Hosting;
    using System.Threading;
    using System.Threading.Tasks;
    using MyApp.Models;
    using MyApp.Repositories;

    public class DiscussionNoteModerationBackgroundService : BackgroundService
    {
        private readonly KafkaNoteConsumerDiscussion _consumer;
        private readonly KafkaNoteStatusProducerDiscussion _producer;
        private readonly IServiceProvider _serviceProvider;

        public DiscussionNoteModerationBackgroundService(
            KafkaNoteConsumerDiscussion consumer,
            KafkaNoteStatusProducerDiscussion producer,
            IServiceProvider serviceProvider)
        {
            _consumer = consumer;
            _producer = producer;
            _serviceProvider = serviceProvider;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            // Вечный цикл обработки сообщений
            await Task.Yield();
            _consumer.Listen(async note =>
            {
                // Модерация
                note.State = note.Content.Contains("badword") ? NoteState.DECLINE : NoteState.APPROVE;

                // Можно сохранить статус в БД, если нужно
                using (var scope = _serviceProvider.CreateScope())
                {
                    var repo = scope.ServiceProvider.GetRequiredService<IDiscussionNoteRepository>();
                    await repo.UpdateAsync(note);
                }

                // Отправка результата обратно
                await _producer.SendStatusAsync(note);
            });
        }
    }
}
