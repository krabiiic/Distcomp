using System.Text.Json;
using Application.Features.Reaction.Commands;
using Application.Features.Reaction.Queries;
using Confluent.Kafka;
using Domain.Mappers;
using Domain.Models;
using Domain.Projections;
using Domain.Repositories;
using Infrastructure.Kafka.Requests;
using MediatR;
using Shared.Domain;
using Shared.Domain.Mappers;
using Shared.Infrastructure.Kafka;

/*public class ReactionHandler : IHandler<Ignore, ReactionRequest>*/
/*{*/
/*    private readonly ILogger<ReactionHandler> _logger;*/
/*    private readonly Random _random = new();*/
/**/
/*    public ReactionHandler(ILogger<ReactionHandler> logger)*/
/*    {*/
/*        _logger = logger;*/
/*    }*/
/**/
/*    public async Task Handle(*/
/*        IReadOnlyCollection<ConsumeResult<Ignore, ReactionRequest>> messages,*/
/*        CancellationToken token*/
/*    )*/
/*    {*/
/*        await Task.Delay(_random.Next(300), token);*/
/*        _logger.LogInformation("Handled {Count} messages", messages.Count);*/
/*    }*/
/*}*/


public class ReactionHandler : IHandler<Ignore, ReactionRequest>
{
    private readonly ILogger<ReactionHandler> _logger;
    private readonly KafkaPublisher<long, ReactionResponse> _responsePublisher;
    private readonly IMediator _mediator;
    private readonly IReactionRepository _reactionRepository;

    public ReactionHandler(
        ILogger<ReactionHandler> logger,
        KafkaPublisher<long, ReactionResponse> responsePublisher,
        IMediator mediator,
        IReactionRepository reactionRepository
    )
    {
        _logger = logger;
        _responsePublisher = responsePublisher;
        _mediator = mediator;
        _reactionRepository = reactionRepository;
    }

    public async Task Handle(
        IReadOnlyCollection<ConsumeResult<Ignore, ReactionRequest>> messages,
        CancellationToken token
    )
    {
        foreach (var message in messages)
        {
            var request = message.Message.Value;
            var correlationId = request.CorrelationId;
            var tweetId = message.Message.Key;

            try
            {
                _logger.LogInformation(
                    "Processing {OperationType} for tweet {TweetId}",
                    request.OperationType,
                    tweetId
                );

                var result = await ProcessRequest(request, token);
                var response = new ReactionResponse
                {
                    CorrelationId = correlationId,
                    Result = result,
                };

                // todo fix the key
                await _responsePublisher.Publish(new[] { ((long)1, response) }, token);

                _logger.LogInformation(
                    "Successfully processed request {CorrelationId}",
                    correlationId
                );
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Failed to process request {CorrelationId}", correlationId);

                /*await _responsePublisher.Publish(*/
                /*    new[] { (correlationId, new ReactionResponse*/
                /*    {*/
                /*        CorrelationId = correlationId,*/
                /*        Result = new { Error = ex.Message }*/
                /*    })},*/
                /*    token*/
                /*);*/
            }
        }
    }

    private async Task<object> ProcessRequest(ReactionRequest request, CancellationToken token)
    {
        switch (request.OperationType)
        {
            case OperationType.Create:
            {
                var createRequest = (
                    (JsonElement)request.Payload
                ).Deserialize<CreateReactionRequest>();
                var reaction = new ReactionModel
                {
                    Id = createRequest.Id,
                    TweetId = createRequest.TweetId,
                    Content = createRequest.Content,
                };

                var result = await _reactionRepository.CreateReaction(reaction);
                if (!result.IsSuccess)
                {
                    return Result.Failure<ReactionProjection>(result.Error);
                }

                return Result
                    .Success<ReactionProjection>(result.Value.ToReactionProjection())
                    .ToResultDto();
            }

            case OperationType.GetById:
            {
                var getByIdRequest = (
                    (JsonElement)request.Payload
                ).Deserialize<GetReactionByIdRequest>();
                var query = new GetReactionByIdQuery(getByIdRequest.Id);

                var result = await _mediator.Send(query, token);
                return result.ToResultDto();
            }

            case OperationType.GetAll:
            {
                var query = new GetReactionsQuery();
                var result = await _mediator.Send(query, token);
                return result.ToResultDto();
            }

            case OperationType.Update:
            {
                var updateRequest = (
                    (JsonElement)request.Payload
                ).Deserialize<UpdateReactionRequest>();
                var command = new UpdateReactionCommand(
                    updateRequest.Id,
                    updateRequest.TweetId,
                    updateRequest.Content
                );
                var result = await _mediator.Send(command, token);
                return result.ToResultDto();
            }

            case OperationType.Delete:
            {
                var deleteRequest = (
                    (JsonElement)request.Payload
                ).Deserialize<DeleteReactionRequest>();
                var command = new DeleteReactionCommand(deleteRequest.Id);

                var result = await _mediator.Send(command, token);
                return result.ToResultDto();
            }

            default:
                throw new NotSupportedException(
                    $"Operation {request.OperationType} is not supported"
                );
        }
        /**/
        /**/
        /*    case OperationType.GetById:*/
        /*    {*/
        /*        var getByIdRequest = (GetReactionByIdRequest)request.Payload;*/
        /*        var result = await _reactionRepository.GetReactionById(getByIdRequest.Id);*/
        /*        return result.IsSuccess ? result.Value : new { Error = result.Error };*/
        /*    }*/
        /**/
        /*    case OperationType.GetAll:*/
        /*    {*/
        /*        var result = await _reactionRepository.GetReactions();*/
        /*        return result.IsSuccess ? result.Value : new { Error = result.Error };*/
        /*    }*/
        /**/
        /*    case OperationType.Update:*/
        /*    {*/
        /*        var updateRequest = (UpdateReactionRequest)request.Payload;*/
        /*        var reaction = new ReactionModel*/
        /*        {*/
        /*            Id = updateRequest.Id,*/
        /*            TweetId = updateRequest.TweetId,*/
        /*            Content = updateRequest.Content,*/
        /*        };*/
        /**/
        /*        var result = await _reactionRepository.UpdateReaction(reaction);*/
        /*        return result.IsSuccess ? result.Value : new { Error = result.Error };*/
        /*    }*/
        /**/
        /*    case OperationType.Delete:*/
        /*    {*/
        /*        var deleteRequest = (DeleteReactionRequest)request.Payload;*/
        /*        var result = await _reactionRepository.DeleteReaction(deleteRequest.Id);*/
        /*        return result.IsSuccess*/
        /*            ? new { Message = "Reaction deleted successfully" }*/
        /*            : new { Error = result.Error };*/
        /*    }*/
        /**/
        /*    default:*/
        /*        throw new NotSupportedException(*/
        /*            $"Operation {request.OperationType} is not supported"*/
        /*        );*/
        /*}*/
    }
}
