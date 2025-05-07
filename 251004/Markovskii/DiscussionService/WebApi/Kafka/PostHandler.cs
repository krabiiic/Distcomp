using System.Net;
using System.Text.Json;
using Application.DTO.Request.Post;
using Application.Interfaces;
using Confluent.Kafka;
using Domain.Entities;
using Domain.Exceptions;
using Infrastructure.Kafka;

namespace WebApi.Kafka;

public class PostHandler: IHandler<Ignore, PostRequest>
{
    private readonly ILogger<PostHandler> _logger;
    private readonly KafkaPublisher<long, PostResponse> _postPublisher;
    private readonly Random _random = new();
    private readonly IPostService _postService;

    public PostHandler(
        ILogger<PostHandler> logger,
        IPostService postService,
        KafkaPublisher<long, PostResponse> postPublisher
    )
    {
        _logger = logger;
        _postService = postService;
        _postPublisher = postPublisher;
    }

    public async Task Handle(
        IReadOnlyCollection<ConsumeResult<Ignore, PostRequest>> messages,
        CancellationToken token
    )
    {
        foreach (var message in messages)
        {
            var request = message.Message.Value;
            var correlationId = request.CorrelationId;
            var postId = message.Message.Key;

            try
            {
                _logger.LogInformation(
                    "Processing {OperationType} for post {correlationId}",
                    request.OperationType,
                    correlationId
                );

                var result = await ProcessRequest(request, token);
                var response = new PostResponse
                {
                    CorrelationId = correlationId,
                    Result = result,
                };
                
                await _postPublisher.Publish(new[] { ((long)1, response) }, token);
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
    
    private async Task<object> ProcessRequest(PostRequest request, CancellationToken token)
    {
        switch (request.OperationType)
        {
            case OperationType.Create:
            {
                var createRequest = (
                    (JsonElement)request.Payload
                ).Deserialize<PostRequestToCreate>();
                return await _postService.CreatePost(createRequest);
            }

            case OperationType.GetById:
            {
                var getByIdRequest = (
                    (JsonElement)request.Payload
                ).Deserialize<PostRequestToGetById>();
                try
                {
                    return await _postService.GetPostById(getByIdRequest);
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
                ).Deserialize<PostRequestToGetAll>();
                return await _postService.GetPosts(getAllRequest);
            }

            case OperationType.Update:
            {
                var updateRequest = (
                    (JsonElement)request.Payload
                ).Deserialize<PostRequestToFullUpdate>();
                
                return await _postService.UpdatePost(updateRequest);
            }

            case OperationType.Delete:
            {
                var deleteRequest = (
                    (JsonElement)request.Payload
                ).Deserialize<PostRequestToDeleteById>();

                try
                {
                    return await _postService.DeletePost(deleteRequest);
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