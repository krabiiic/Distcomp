using System.Net;
using System.Text.Json;
using Core.DTO;
using Core.Interfaces;
using Confluent.Kafka;
using Core.Exceptions;
using Core.Kafka;

namespace WebApi.Kafka;

public class MessageHandler: IHandler<Ignore, MessageRequest>
{
    private readonly ILogger<MessageHandler> _logger;
    private readonly KafkaPublisher<long, MessageResponse> _messagePublisher;
    private readonly Random _random = new();
    private readonly IMessageService _messageService;

    public MessageHandler(
        ILogger<MessageHandler> logger,
        IMessageService messageService,
        KafkaPublisher<long, MessageResponse> messagePublisher
    )
    {
        _logger = logger;
        _messageService = messageService;
        _messagePublisher = messagePublisher;
    }

    public async Task Handle(
        IReadOnlyCollection<ConsumeResult<Ignore, MessageRequest>> messages,
        CancellationToken token
    )
    {
        foreach (var message in messages)
        {
            var request = message.Message.Value;
            var correlationId = request.CorrelationId;
            var messageId = message.Message.Key;

            try
            {
                _logger.LogInformation(
                    "Processing {OperationType} for message {correlationId}",
                    request.OperationType,
                    correlationId
                );

                var result = await ProcessRequest(request, token);
                var response = new MessageResponse
                {
                    CorrelationId = correlationId,
                    Result = result,
                };
                
                await _messagePublisher.Publish(new[] { ((long)1, response) }, token);
                _logger.LogInformation(
                    "Successfully processed request {CorrelationId} with response {Result}",
                    correlationId,
                    response.Result
                );
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Failed to process request {CorrelationId}", correlationId);
            }
        }
    }
    
    private async Task<object> ProcessRequest(MessageRequest request, CancellationToken token)
    {
        switch (request.OperationType)
        {
            case OperationType.Create:
            {
                var createRequest = (
                    (JsonElement)request.Payload
                ).Deserialize<MessageRequestToCreate>();
                return await _messageService.CreateMessage(createRequest);
            }

            case OperationType.GetById:
            {
                var getByIdRequest = (
                    (JsonElement)request.Payload
                ).Deserialize<MessageRequestToGetById>();
                try
                {
                    return await _messageService.GetMessageById(getByIdRequest);
                }
                catch (NotFoundException e)
                {
                    return HttpStatusCode.NotFound;
                }
            }

            case OperationType.GetAll:
            {
                var getAllRequest = (
                    (JsonElement)request.Payload
                ).Deserialize<MessageRequestToGetAll>();
                return await _messageService.GetMessages(getAllRequest);
            }

            case OperationType.Update:
            {
                var updateRequest = (
                    (JsonElement)request.Payload
                ).Deserialize<MessageRequestToFullUpdate>();
                
                return await _messageService.UpdateMessage(updateRequest);
            }

            case OperationType.Delete:
            {
                var deleteRequest = (
                    (JsonElement)request.Payload
                ).Deserialize<MessageRequestToDeleteById>();

                try
                {
                    return await _messageService.DeleteMessage(deleteRequest);
                }
                catch (NotFoundException e)
                {
                    return HttpStatusCode.NotFound;
                }
            }

            default:
                throw new NotSupportedException(
                    $"Operation {request.OperationType} is not supported"
                );
        }
    }
}