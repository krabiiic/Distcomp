using Confluent.Kafka;
using Core;
using System;
using System.Text.Json;

namespace API.Kafka
{
    public class NoteMessageConsumer : BackgroundService
    {
        private readonly IConsumer<Ignore, KafkaMessage> _consumer;
        private readonly NoteResponseListener _listener;

        public NoteMessageConsumer(IConfiguration configuration, NoteResponseListener listener)
        {
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

            _consumer = new ConsumerBuilder<Ignore, KafkaMessage>(config)
                .SetValueDeserializer(new NoteMessageJsonDeserializer())
                .Build();

            _listener = listener;
        }

        protected override Task ExecuteAsync(CancellationToken stoppingToken)
        {
            return Task.Run(() =>
            {
                _consumer.Subscribe("OutTopic");

                while (!stoppingToken.IsCancellationRequested)
                {
                    try
                    {
                        var result = _consumer.Consume(stoppingToken);
                        Console.WriteLine($" -/-/-/-/-/- CONSUMER id = {result.Value.RequestId} at {DateTime.Now:HH:mm:ss.fff}");
                        _listener.HandleResponse(result.Message.Value);
                    }
                    catch (ConsumeException ex)
                    {
                        Console.WriteLine($"Kafka consume error: {ex.Error.Reason}");
                    }
                }

            }, stoppingToken);
        }
    }
}
