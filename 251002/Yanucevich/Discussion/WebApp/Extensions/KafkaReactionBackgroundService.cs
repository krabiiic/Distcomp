using System.Text.Json;
using System.Text.Json.Serialization;
using Confluent.Kafka;
using Infrastructure.Serialization;
using Infrastructure.Settings;
using Microsoft.Extensions.Options;

public class KafkaReactionBackgroundService : BackgroundService
{
    private readonly KafkaAsyncConsumer<Ignore, ReactionRequest> _consumer;
    private readonly ILogger<KafkaReactionBackgroundService> _logger;

    public KafkaReactionBackgroundService(
        IServiceProvider serviceProvider,
        IOptions<KafkaOptions> options,
        ILogger<KafkaReactionBackgroundService> logger,
        ILogger<KafkaAsyncConsumer<Ignore, ReactionRequest>> consumerLogger
    )
    {
        var kafkaOptions = options.Value;
        _logger = logger;
        var handler = serviceProvider.GetRequiredService<ReactionHandler>();

        var serializer = new SystemTextJsonSerializer<ReactionRequest>(
            new JsonSerializerOptions { Converters = { new JsonStringEnumConverter() } }
        );

        _consumer = new KafkaAsyncConsumer<Ignore, ReactionRequest>(
            kafkaOptions.BootstrapServer,
            kafkaOptions.ReactionEventsTopic,
            kafkaOptions.GroupId,
            handler,
            null,
            serializer,
            consumerLogger
        );
    }

    public override Task StopAsync(CancellationToken cancellationToken)
    {
        _consumer.Dispose();

        return Task.CompletedTask;
    }

    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        try
        {
            await _consumer.Consume(stoppingToken);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Unhandled exception occured");
        }
    }
}
