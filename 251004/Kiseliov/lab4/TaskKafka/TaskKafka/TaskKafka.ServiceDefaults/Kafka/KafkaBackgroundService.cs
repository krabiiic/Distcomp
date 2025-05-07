using Confluent.Kafka;
using Confluent.Kafka.Admin;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using TaskKafka.ServiceDefaults.Kafka.Consumer;
namespace TaskKafka.ServiceDefaults.Kafka;

public sealed class KafkaBackgroundService<TK, TV>(
    IConsumer<TK, TV> consumer,
    IKafkaHandler<TK, TV> handler,
    IOptions<KafkaHandlerSettings<TK, TV>> options,
    ILogger<KafkaBackgroundService<TK, TV>> logger) : BackgroundService
    where TK : class
    where TV : class
{
    private readonly KafkaHandlerSettings<TK, TV> _settings = options.Value;

    public override async Task StartAsync(CancellationToken cancellationToken)
    {
        await CreateTopicIfNotExists(_settings.Topic);

        await base.StartAsync(cancellationToken);
    }

    protected override Task ExecuteAsync(CancellationToken stoppingToken) => Task.Factory.StartNew(
        () => StartConsumerLoop(stoppingToken),
        stoppingToken,
        TaskCreationOptions.LongRunning,
        TaskScheduler.Default);

    private async Task StartConsumerLoop(CancellationToken stoppingToken)
    {
        consumer.Subscribe(_settings.Topic);

        while (!stoppingToken.IsCancellationRequested)
        {
            try
            {
                var consumeResult = consumer.Consume(stoppingToken);

                if (consumeResult is null || consumeResult.IsPartitionEOF || consumeResult.Message.Value is null)
                    continue;

                logger.LogInformation("Consuming message: {Value}", consumeResult.Message.Value);

                await handler.HandleAsync(consumeResult.Message.Key, consumeResult.Message.Value);

                logger.LogInformation("Message consumed"); ;
            }
            catch (OperationCanceledException)
            {
                logger.LogInformation("Consumer loop canceled");
                break;
            }
            catch (KafkaException e)
            {
                logger.LogError(e, "Error consuming message");
            }
        }
    }

    private async Task CreateTopicIfNotExists(string topicName)
    {
        var adminClientBuilder = new AdminClientBuilder(new AdminClientConfig
        {
            BootstrapServers = _settings.ConsumerConfig.BootstrapServers
        });
        adminClientBuilder.SetLogHandler((client, log) => logger.LogInformation("Admin message: {Message}", log.Message));
        using IAdminClient adminClient = adminClientBuilder.Build();
        try
        {
            await adminClient.CreateTopicsAsync(new[]
            {
                new TopicSpecification
                {
                    Name = topicName,
                    NumPartitions = 1,
                    ReplicationFactor = 1
                }
            });
        }
        catch (CreateTopicsException e)
        {
            if (e.Results[0].Error.Code != ErrorCode.TopicAlreadyExists)
            {
                logger.LogError(e, "An error occurred creating topic {TopicName}", topicName);
                throw;
            }
            logger.LogInformation("Topic {TopicName} already exists", topicName);
        }
    }

    public override void Dispose()
    {
        consumer.Close();
        consumer.Dispose();

        base.Dispose();
    }
}
