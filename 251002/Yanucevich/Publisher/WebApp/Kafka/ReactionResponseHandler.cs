using Confluent.Kafka;
using Shared.Infrastructure.Kafka;

public class ReactionResponseHandler : IHandler<Ignore, ReactionResponse>
{
    private readonly ILogger<ReactionResponseHandler> _logger;
    private readonly KafkaResponseDispatcher<ReactionResponse> _dispatcher;
    private readonly Random _random = new();

    public ReactionResponseHandler(
        ILogger<ReactionResponseHandler> logger,
        KafkaResponseDispatcher<ReactionResponse> dispatcher
    )
    {
        _logger = logger;
        _dispatcher = dispatcher;
    }

    public async Task Handle(
        IReadOnlyCollection<ConsumeResult<Ignore, ReactionResponse>> messages,
        CancellationToken token
    )
    {
        foreach (var message in messages)
        {
            _logger.LogInformation($"acquired response for {message.Value.CorrelationId}");
            var response = message.Message.Value;
            // request handling
            _dispatcher.TryCompleteRequest(response.CorrelationId, response);

            /*await Task.Delay(_random.Next(300), token);*/
            /*_logger.LogInformation("Handled {Count} messages", messages.Count);*/
        }
    }
}
