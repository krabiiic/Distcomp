using System;
using System.Reflection.Metadata;
using System.Text.Json;
using Application.Features.Reaction.Commands;
using Asp.Versioning;
using Confluent.Kafka;
using Domain.Projections;
using Infrastructure.Kafka.Requests;
using MediatR;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Settings;
using Shared.Domain;
using Shared.Domain.Mappers;
using Shared.Infrastructure.Kafka;
using SocialNet.Abstractions;

namespace Discussion.Controllers;

[Route("reactions")]
[ApiVersion("1.0")]
public class ReactionController : MediatrController
{
    private readonly KafkaPublisher<long, ReactionRequest> _kafkaPublisher;
    private readonly KafkaResponseDispatcher<ReactionResponse> _dispatcher;

    private readonly IRedisCacheService _redis;
    private readonly string _cache_key_prefix = "reaction";
    private readonly TimeSpan _cache_expiration = TimeSpan.FromSeconds(10);

    private long _id = 1;

    private long getNextId()
    {
        return _id++;
    }

    public ReactionController(
        KafkaPublisher<long, ReactionRequest> kafkaPublisher,
        KafkaResponseDispatcher<ReactionResponse> dispatcher,
        IMediator mediator,
        IRedisCacheService redis
    )
        : base(mediator)
    {
        _kafkaPublisher = kafkaPublisher;
        _dispatcher = dispatcher;
        _redis = redis;
    }

    [HttpPost]
    public async Task<IActionResult> CreateReaction(CreateReactionCommand command)
    {
        var correlationId = Guid.NewGuid().ToString();
        var nextId = getNextId();
        var message = new Message<long, ReactionRequest>
        {
            Key = command.tweetId,
            Value = new ReactionRequest
            {
                CorrelationId = correlationId,
                OperationType = OperationType.Create,
                Payload = new CreateReactionRequest
                {
                    Id = nextId,
                    TweetId = command.tweetId,
                    Content = command.content,
                },
            },
        };

        /*var tcs = new TaskCompletionSource<ReactionResponse>();*/
        /*_dispatcher.RegisterRequest(correlationId, tcs);*/
        /**/
        await _kafkaPublisher.Publish(
            new[] { (message.Key, message.Value) },
            CancellationToken.None
        );

        /*var timeoutTask = Task.Delay(TimeSpan.FromSeconds(1));*/
        /*var completedTask = await Task.WhenAny(tcs.Task, timeoutTask);*/
        /**/
        /*if (completedTask == timeoutTask)*/
        /*{*/
        /*    return StatusCode(504, "Request timed out");*/
        /*}*/
        /**/
        /*var response = await tcs.Task;*/
        /**/
        /*return Ok(response.Result);*/
        return StatusCode(
            StatusCodes.Status201Created,
            new ReactionProjection
            {
                Id = nextId,
                TweetId = command.tweetId,
                Content = command.content,
            }
        );
    }

    [HttpGet]
    public async Task<IActionResult> GetReactions()
    {
        var correlationId = Guid.NewGuid().ToString();
        var message = new Message<long, ReactionRequest>
        {
            Key = 0,
            Value = new ReactionRequest
            {
                CorrelationId = correlationId,
                OperationType = OperationType.GetAll,
                Payload = new GetReactionsRequest { },
            },
        };

        var tcs = new TaskCompletionSource<ReactionResponse>();
        _dispatcher.RegisterRequest(correlationId, tcs);

        await _kafkaPublisher.Publish(
            new[] { (message.Key, message.Value) },
            CancellationToken.None
        );

        var timeoutTask = Task.Delay(TimeSpan.FromSeconds(1));
        var completedTask = await Task.WhenAny(tcs.Task, timeoutTask);

        if (completedTask == timeoutTask)
        {
            return StatusCode(504, "Request timed out");
        }
        var response = await tcs.Task;
        Result<IEnumerable<ReactionProjection>> responseResult = ((JsonElement)response.Result)
            .Deserialize<ResultDto<IEnumerable<ReactionProjection>>>()
            .ToResult();

        if (!responseResult.IsSuccess)
        {
            return HandleFailure(responseResult);
        }

        return StatusCode(StatusCodes.Status200OK, responseResult.Value);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetReactionByIdQuery(long id)
    {
        var cached_result = await _redis.GetCacheValueAsync<ReactionProjection>(
            $"{_cache_key_prefix}:{id}"
        );

        if (cached_result is not null)
        {
            return StatusCode(StatusCodes.Status200OK, cached_result);
        }

        var correlationId = Guid.NewGuid().ToString();
        var message = new Message<long, ReactionRequest>
        {
            Key = 0,
            Value = new ReactionRequest
            {
                CorrelationId = correlationId,
                OperationType = OperationType.GetById,
                Payload = new GetReactionByIdRequest { Id = id },
            },
        };

        var tcs = new TaskCompletionSource<ReactionResponse>();
        _dispatcher.RegisterRequest(correlationId, tcs);

        await _kafkaPublisher.Publish(
            new[] { (message.Key, message.Value) },
            CancellationToken.None
        );

        var timeoutTask = Task.Delay(TimeSpan.FromSeconds(1));
        var completedTask = await Task.WhenAny(tcs.Task, timeoutTask);

        if (completedTask == timeoutTask)
        {
            return StatusCode(504, "Request timed out");
        }

        var response = await tcs.Task;
        Result<ReactionProjection> responseResult = ((JsonElement)response.Result)
            .Deserialize<ResultDto<ReactionProjection>>()
            .ToResult();

        if (!responseResult.IsSuccess)
        {
            return HandleFailure(responseResult);
        }

        await _redis.SetCacheValueAsync<ReactionProjection>(
            $"{_cache_key_prefix}:{id}",
            responseResult.Value,
            _cache_expiration
        );

        return StatusCode(StatusCodes.Status200OK, responseResult.Value);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateReaction(UpdateReactionCommand command)
    {
        var correlationId = Guid.NewGuid().ToString();
        var message = new Message<long, ReactionRequest>
        {
            Key = command.tweetId,
            Value = new ReactionRequest
            {
                CorrelationId = correlationId,
                OperationType = OperationType.Update,
                Payload = new UpdateReactionRequest
                {
                    Id = command.id,
                    TweetId = command.tweetId,
                    Content = command.content,
                },
            },
        };

        var tcs = new TaskCompletionSource<ReactionResponse>();
        _dispatcher.RegisterRequest(correlationId, tcs);

        await _kafkaPublisher.Publish(
            new[] { (message.Key, message.Value) },
            CancellationToken.None
        );

        var timeoutTask = Task.Delay(TimeSpan.FromSeconds(1));
        var completedTask = await Task.WhenAny(tcs.Task, timeoutTask);

        if (completedTask == timeoutTask)
        {
            return StatusCode(504, "Request timed out");
        }

        var response = await tcs.Task;

        Result<ReactionProjection> responseResult = ((JsonElement)response.Result)
            .Deserialize<ResultDto<ReactionProjection>>()
            .ToResult();

        if (!responseResult.IsSuccess)
        {
            return HandleFailure(responseResult);
        }

        var redisKey = $"{_cache_key_prefix}:{responseResult.Value.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);

        return StatusCode(StatusCodes.Status200OK, responseResult.Value);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteReaction(long id)
    {
        var correlationId = Guid.NewGuid().ToString();
        var message = new Message<long, ReactionRequest>
        {
            Key = 0,
            Value = new ReactionRequest
            {
                CorrelationId = correlationId,
                OperationType = OperationType.Delete,
                Payload = new DeleteReactionRequest { Id = id },
            },
        };

        var tcs = new TaskCompletionSource<ReactionResponse>();
        _dispatcher.RegisterRequest(correlationId, tcs);

        await _kafkaPublisher.Publish(
            new[] { (message.Key, message.Value) },
            CancellationToken.None
        );

        var timeoutTask = Task.Delay(TimeSpan.FromSeconds(1));
        var completedTask = await Task.WhenAny(tcs.Task, timeoutTask);

        if (completedTask == timeoutTask)
        {
            return StatusCode(504, "Request timed out");
        }

        var response = await tcs.Task;
        Result responseResult = ((JsonElement)response.Result).Deserialize<ResultDto>().ToResult();

        if (!responseResult.IsSuccess)
        {
            return HandleFailure(responseResult);
        }

        var redisKey = $"{_cache_key_prefix}:{id}";
        await _redis.RemoveCacheValueAsync(redisKey);

        return StatusCode(StatusCodes.Status204NoContent);
    }
}

/*[Route("reactions")]*/
/*[ApiVersion("1.0")]*/
/*public class ReactionController : MediatrController*/
/*{*/
/*    private readonly HttpClient _httpClient;*/
/*    private readonly DiscussionSettings _discussionSettings;*/
/**/
/*    public ReactionController(*/
/*        IMediator mediator,*/
/*        HttpClient httpClient,*/
/*        IOptions<DiscussionSettings> discussionSettings*/
/*    )*/
/*        : base(mediator)*/
/*    {*/
/*        _httpClient = httpClient;*/
/*        _discussionSettings = discussionSettings.Value;*/
/*    }*/
/**/
/*    [HttpPost]*/
/*    public async Task<IActionResult> CreateReaction([FromBody] object requestBody)*/
/*    {*/
/*        var microserviceUrl = $"{_discussionSettings.DiscussionUrl}/api/v1.0/reactions/";*/
/**/
/*        var response = await _httpClient.PostAsJsonAsync(microserviceUrl, requestBody);*/
/**/
/*        if (response.IsSuccessStatusCode)*/
/*        {*/
/*            var content = await response.Content.ReadAsStringAsync();*/
/*            return StatusCode((int)response.StatusCode, content);*/
/*        }*/
/*        else*/
/*        {*/
/*            return StatusCode((int)response.StatusCode, await response.Content.ReadAsStringAsync());*/
/*        }*/
/*    }*/
/**/
/*    [HttpGet]*/
/*    public async Task<IActionResult> GetReactions()*/
/*    {*/
/*        var microserviceUrl = $"{_discussionSettings.DiscussionUrl}/api/v1.0/reactions/";*/
/**/
/*        var response = await _httpClient.GetAsync(microserviceUrl);*/
/**/
/*        if (response.IsSuccessStatusCode)*/
/*        {*/
/*            var content = await response.Content.ReadAsStringAsync();*/
/*            return StatusCode((int)response.StatusCode, content);*/
/*        }*/
/*        else*/
/*        {*/
/*            return StatusCode((int)response.StatusCode, await response.Content.ReadAsStringAsync());*/
/*        }*/
/*    }*/
/**/
/*    [HttpGet("{id}")]*/
/*    public async Task<IActionResult> GetReactionByIdQuery(string id)*/
/*    {*/
/*        var microserviceUrl = $"{_discussionSettings.DiscussionUrl}/api/v1.0/reactions/{id}";*/
/**/
/*        var response = await _httpClient.GetAsync(microserviceUrl);*/
/**/
/*        if (response.IsSuccessStatusCode)*/
/*        {*/
/*            var content = await response.Content.ReadAsStringAsync();*/
/*            return StatusCode((int)response.StatusCode, content);*/
/*        }*/
/*        else*/
/*        {*/
/*            return StatusCode((int)response.StatusCode, await response.Content.ReadAsStringAsync());*/
/*        }*/
/*    }*/
/**/
/*    [HttpPut]*/
/*    public async Task<IActionResult> UpdateReaction([FromBody] object requestBody)*/
/*    {*/
/*        var microserviceUrl = $"{_discussionSettings.DiscussionUrl}/api/v1.0/reactions/";*/
/**/
/*        var response = await _httpClient.PutAsJsonAsync(microserviceUrl, requestBody);*/
/**/
/*        if (response.IsSuccessStatusCode)*/
/*        {*/
/*            var content = await response.Content.ReadAsStringAsync();*/
/*            return StatusCode((int)response.StatusCode, content);*/
/*        }*/
/*        else*/
/*        {*/
/*            return StatusCode((int)response.StatusCode, await response.Content.ReadAsStringAsync());*/
/*        }*/
/*    }*/
/**/
/*    [HttpDelete("{id}")]*/
/*    public async Task<IActionResult> DeleteReaction(string id)*/
/*    {*/
/*        var microserviceUrl = $"{_discussionSettings.DiscussionUrl}/api/v1.0/reactions/{id}";*/
/**/
/*        var response = await _httpClient.DeleteAsync(microserviceUrl);*/
/**/
/*        if (response.IsSuccessStatusCode)*/
/*        {*/
/*            var content = await response.Content.ReadAsStringAsync();*/
/*            return StatusCode((int)response.StatusCode, content);*/
/*        }*/
/*        else*/
/*        {*/
/*            return StatusCode((int)response.StatusCode, await response.Content.ReadAsStringAsync());*/
/*        }*/
/*    }*/
/*}*/
