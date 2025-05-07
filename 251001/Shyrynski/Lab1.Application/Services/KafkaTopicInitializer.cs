using Confluent.Kafka.Admin;
using Microsoft.Extensions.Hosting;
using Confluent.Kafka;

public class KafkaTopicInitializer : IHostedService
{
    private readonly string _bootstrapServers = "kafka:9092"; // Можно вынести в конфиг
    private readonly List<string> _topics = new() { "InTopic", "OutTopic" }; // Укажи нужные топики

    public async Task StartAsync(CancellationToken cancellationToken)
    {
        using var adminClient = new AdminClientBuilder(new AdminClientConfig { BootstrapServers = _bootstrapServers }).Build();

        try
        {
            var metadata = adminClient.GetMetadata(TimeSpan.FromSeconds(5));
            var existingTopics = metadata.Topics.Select(t => t.Topic).ToHashSet();

            var topicsToCreate = _topics.Where(t => !existingTopics.Contains(t)).ToList();

            if (topicsToCreate.Any())
            {
                await adminClient.CreateTopicsAsync(topicsToCreate.Select(topic => new TopicSpecification
                {
                    Name = topic,
                    NumPartitions = 1,
                    ReplicationFactor = 1
                }));
                Console.WriteLine($"✅ Созданы топики: {string.Join(", ", topicsToCreate)}");
            }
            else
            {
                Console.WriteLine("✅ Все топики уже существуют.");
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"⚠️ Ошибка при создании топиков: {ex.Message}");
        }
    }

    public Task StopAsync(CancellationToken cancellationToken) => Task.CompletedTask;
}
