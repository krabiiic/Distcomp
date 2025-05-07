using System.Text.Json;
using System.Text.Json.Serialization;
using Confluent.Kafka;
using Infrastructure.Serialization;
using Infrastructure.Settings;
using Microsoft.Extensions.Options;

public class KafkaResponseBackgroundService : BackgroundService
{
    private readonly KafkaAsyncConsumer<Ignore, ReactionResponse> _consumer;
    private readonly ILogger<KafkaResponseBackgroundService> _logger;

    public KafkaResponseBackgroundService(
        IServiceProvider serviceProvider,
        IOptions<KafkaOptions> options,
        ILogger<KafkaResponseBackgroundService> logger
    )
    {
        var kafkaOptions = options.Value;
        _logger = logger;
        var handler = serviceProvider.GetRequiredService<ReactionResponseHandler>();

        var serializer = new SystemTextJsonSerializer<ReactionResponse>(
            new JsonSerializerOptions { Converters = { new JsonStringEnumConverter() } }
        );

        _consumer = new KafkaAsyncConsumer<Ignore, ReactionResponse>(
            kafkaOptions.BootstrapServer,
            kafkaOptions.ReactionResponsesTopic,
            kafkaOptions.GroupId,
            handler,
            null,
            serializer
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
