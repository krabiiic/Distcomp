using Confluent.Kafka.Admin;
using Microsoft.Extensions.Hosting;
using Confluent.Kafka;

// Обеспечение наличия топиков InTopic, OutTopic
public class KafkaTopicInitializer : IHostedService
{
    private readonly string _bootstrapServers = "kafka:9092"; 
    private readonly List<string> _topics = new() { "InTopic", "OutTopic" };

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
            }
            else
            {
            }
        }
        catch (Exception ex)
        {
        }
    }

    public Task StopAsync(CancellationToken cancellationToken) => Task.CompletedTask;
}
