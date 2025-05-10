using Confluent.Kafka;
using Infrastructure.Kafka;

namespace WebApi.Kafka;

public class PostResponseHandler: IHandler<Ignore, PostResponse>
{
    private readonly ILogger<PostResponseHandler> _logger;
    private readonly KafkaResponseDispatcher<PostResponse> _dispatcher;
    private readonly Random _random = new();

    public PostResponseHandler(
        ILogger<PostResponseHandler> logger,
        KafkaResponseDispatcher<PostResponse> dispatcher
    )
    {
        _logger = logger;
        _dispatcher = dispatcher;
    }

    public async Task Handle(
        IReadOnlyCollection<ConsumeResult<Ignore, PostResponse>> messages,
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