using Confluent.Kafka;
using Lab1.Core.Abstractions;
using Lab1.Core.Contracts;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using System.Collections.Concurrent;
using System.Text.Json;

namespace Lab1.Application.Services
{
    public class KafkaConsumer : IConsumer
    {
        private readonly IConsumer<string, string> _consumer;
        private readonly ConcurrentDictionary<string, TaskCompletionSource<MessageResponse>> _pendingResponses = new();
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

            _consumer = new ConsumerBuilder<string, string>(config).Build();
            _consumer.Subscribe("OutTopic");
        }

        public Task StartAsync(CancellationToken cancellationToken)
        {
            _cts = CancellationTokenSource.CreateLinkedTokenSource(cancellationToken);
            _executingTask = Task.Run(() => StartConsuming(_cts.Token), _cts.Token);
            Console.WriteLine("Kafka consumer started.");
            return Task.CompletedTask;
        }

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
            Console.WriteLine("Kafka consumer stopped.");
        }

        private void StartConsuming(CancellationToken cancellationToken)
        {
            try
            {
                while (!cancellationToken.IsCancellationRequested)
                {
                    var consumeResult = _consumer.Consume(cancellationToken);

                    if (consumeResult == null)
                    {
                        continue;
                    }

                    var response = JsonSerializer.Deserialize<MessageResponse>(consumeResult.Message.Value);
                    if (response == null)
                    {
                        continue;
                    }
                    if (_pendingResponses.TryRemove(response.Id, out var tcs))
                    {
                        tcs.TrySetResult(response);
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Ошибка в консьюмере: {ex.Message}\n{ex.StackTrace}");
            }
        }

        public async Task<MessageResponse?> WaitForResponseAsync(string messageId, TimeSpan timeout)
        {
            var tcs = new TaskCompletionSource<MessageResponse>();

            _pendingResponses.TryAdd(messageId, tcs);

            var delayTask = Task.Delay(timeout);
            var responseTask = tcs.Task;

            var completedTask = await Task.WhenAny(responseTask, delayTask);

            if (completedTask == delayTask)
            {
                _pendingResponses.TryRemove(messageId, out _);
                return null;
            }

            return await responseTask;
        }
    }
}
