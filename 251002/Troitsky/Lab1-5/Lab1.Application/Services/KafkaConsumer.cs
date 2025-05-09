using Confluent.Kafka;
using Lab1.Core.Abstractions;
using Lab1.Core.Contracts;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using System.Collections.Concurrent;
using System.Text.Json;

namespace Lab1.Application.Services
{
    // Логика потребления сообщений из kafka с асинхронным ожиданием сообщений
    public class KafkaConsumer : IConsumer
    {
        private readonly IConsumer<string, string> _consumer;
        // Потокобезопасный словарь, через который пробуждается ожидающая задача при получении ответа
        private readonly ConcurrentDictionary<string, TaskCompletionSource<PostResponse>> _pendingResponses = new();
        // Задача чтения из кафка
        private Task? _executingTask;
        private CancellationTokenSource? _cts;

        public KafkaConsumer()
        {
            var config = new ConsumerConfig
            {
                BootstrapServers = "kafka:9092",
                GroupId = "web-consumer-group",
                AutoOffsetReset = AutoOffsetReset.Earliest
            };

            // Подписка на топик OutTopic и получение от него всех сообщений
            _consumer = new ConsumerBuilder<string, string>(config).Build();
            _consumer.Subscribe("OutTopic");
        }

        public Task StartAsync(CancellationToken cancellationToken)
        {
            // При изменении внешнего токена отменяется задача
            _cts = CancellationTokenSource.CreateLinkedTokenSource(cancellationToken);
            // Запуск задачи ожидания сообщений
            _executingTask = Task.Run(() => StartDiscussing(_cts.Token), _cts.Token);
            // Синхронный запуск фоновой задачи
            return Task.CompletedTask;
        }

        // Завершение ожидания сообщений из Kafka
        public async Task StopAsync(CancellationToken cancellationToken)
        {
            if (_cts != null)
            {
                _cts.Cancel();
                _cts.Dispose();
            }

            if (_executingTask != null)
            {
                await _executingTask;
            }

            _consumer.Close();
        }

        private void StartDiscussing(CancellationToken cancellationToken)
        {
            try
            {
                while (!cancellationToken.IsCancellationRequested)
                {
                    // Блокирует для ожидания сообщения/ возвращает сразу, если пришло
                    var consumerResult = _consumer.Consume(cancellationToken);

                    if (consumerResult == null)
                    {
                        continue;
                    }

                    var response = JsonSerializer.Deserialize<PostResponse>(consumerResult.Message.Value); // Message.Value - стандартные в ConsumeResult
                    if (response == null)
                    {
                        continue;
                    }
                    // Попытка удаления из словаря, в котором хранятся асинхронные ожидания ответов
                    if (_pendingResponses.TryRemove(response.Id, out var tcs))
                    {
                        tcs.TrySetResult(response);
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Ошибка в consumer: {ex.Message}\n{ex.StackTrace}");
            }
        }

        //  Асинхронное ожидание ответа, привязанного к конкретному идентификатору сообщения
        public async Task<PostResponse?> WaitForResponseAsync(string messageId, TimeSpan timeout)
        {
            var tcs = new TaskCompletionSource<PostResponse>();

            _pendingResponses.TryAdd(messageId, tcs);

            var delayTask = Task.Delay(timeout);
            var responseTask = tcs.Task;

            var completedTask = await Task.WhenAny(responseTask, delayTask);

            // Если время вышло, удаляется из словаря, иначе удалится в цикле ожидания сообщений Consumer
            if (completedTask == delayTask)
            {
                _pendingResponses.TryRemove(messageId, out _);
                return null;
            }

            // Если вовремя завершилась - возвращается результат ожидания 
            return await responseTask;
        }
    }
}
