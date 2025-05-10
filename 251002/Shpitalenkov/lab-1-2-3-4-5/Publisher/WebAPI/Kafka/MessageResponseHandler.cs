using Confluent.Kafka;
using Core.Kafka;

namespace WebApi.Kafka;

public class MessageResponseHandler: IHandler<Ignore, MessageResponse>
{
    private readonly ILogger<MessageResponseHandler> _logger;
    private readonly KafkaResponseDispatcher<MessageResponse> _dispatcher;
    private readonly Random _random = new();

    public MessageResponseHandler(
        ILogger<MessageResponseHandler> logger,
        KafkaResponseDispatcher<MessageResponse> dispatcher
    )
    {
        _logger = logger;
        _dispatcher = dispatcher;
    }

    public async Task Handle(
        IReadOnlyCollection<ConsumeResult<Ignore, MessageResponse>> messages,
        CancellationToken token
    )
    {
        foreach (var message in messages)
        {
            _logger.LogInformation($"acquired response for {message.Value.CorrelationId}");
            var response = message.Message.Value;
            _dispatcher.TryCompleteRequest(response.CorrelationId, response);
        }
    }
}