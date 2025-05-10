using System.Net;
using System.Text.Json;
using Application.DTO.Request.Post;
using Application.DTO.Response.Post;
using Confluent.Kafka;
using Domain.Exceptions;
using Infrastructure.Kafka;
using Infrastructure.Repositories.Redis;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using WebApi.Settings;

namespace WebApi.Controllers;

[ApiController]
[Route("/api/v1.0/posts")]
public class PostController : ControllerBase
{
    private readonly IRedisCacheService _redis;
    private readonly KafkaPublisher<long, PostRequest> _kafkaPublisher;
    private readonly KafkaResponseDispatcher<PostResponse> _dispatcher;

    private readonly string _cachePrefix = "post";
    private readonly TimeSpan _cacheExpiration = TimeSpan.FromSeconds(10);
    
    private long _id = 1;

    private long getNextId()
    {
        return _id++;
    }
    
    public PostController(
        KafkaPublisher<long, PostRequest> kafkaPublisher,
        KafkaResponseDispatcher<PostResponse> dispatcher,
        IRedisCacheService redis
    )
    {
        _kafkaPublisher = kafkaPublisher;
        _dispatcher = dispatcher;
        _redis = redis;
    }

    [HttpPost]
    public async Task<IActionResult> CreatePost(PostRequestToCreate postRequestToCreate)
    {
        var correlationId = Guid.NewGuid().ToString();
        var nextId = getNextId();
        var message = new Message<long, PostRequest>
        {
            Key = postRequestToCreate.NewsId,
            Value = new PostRequest()
            {
                CorrelationId = correlationId,
                OperationType = OperationType.Create,
                Payload = new PostRequestToFullUpdate()
                {
                    Id = nextId,
                    NewsId = postRequestToCreate.NewsId,
                    Content = postRequestToCreate.Content,
                },
            },
        };
        await _kafkaPublisher.Publish(
            new[] { (message.Key, message.Value) },
            CancellationToken.None
        );
        
        return StatusCode(
            StatusCodes.Status201Created,
            new PostResponseToGetById()
            {
                Id = nextId,
                NewsId = postRequestToCreate.NewsId,
                Content = postRequestToCreate.Content,
            }
        );
    }

    [HttpGet]
    public async Task<IActionResult> GetPosts()
    {
        var correlationId = Guid.NewGuid().ToString();
        var message = new Message<long, PostRequest>
        {
            Key = 0,
            Value = new PostRequest
            {
                CorrelationId = correlationId,
                OperationType = OperationType.GetAll,
                Payload = new PostRequestToGetAll(),
            },
        };

        var tcs = new TaskCompletionSource<PostResponse>();
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
        
        Console.WriteLine(((JsonElement)response.Result).ToString());
        var responseResult = ((JsonElement)response.Result)
            .Deserialize<IEnumerable<PostResponseToGetById?>?>();
        

        return StatusCode(StatusCodes.Status200OK, responseResult);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetPostByIdQuery(long id)
    {
        var cachedResult = await _redis.GetCacheValueAsync<PostResponseToGetById?>(
            $"{_cachePrefix}:{id}"
        );

        if (cachedResult is not null)
        {
            return StatusCode(StatusCodes.Status200OK, cachedResult);
        }
        
        var correlationId = Guid.NewGuid().ToString();
        var message = new Message<long, PostRequest>
        {
            Key = 0,
            Value = new PostRequest
            {
                CorrelationId = correlationId,
                OperationType = OperationType.GetById,
                Payload = new PostRequestToGetById(){Id = id},
            },
        };

        var tcs = new TaskCompletionSource<PostResponse>();
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
        try
        {
            PostResponseToGetById responseResult = ((JsonElement)response.Result)
                .Deserialize<PostResponseToGetById>();
            
            await _redis.SetCacheValueAsync<PostResponseToGetById>(
                $"{_cachePrefix}:{id}",
                responseResult,
                _cacheExpiration
            );
            
            return StatusCode(StatusCodes.Status200OK, responseResult);
        }
        catch
        {
            HttpStatusCode statusCode = ((JsonElement)response.Result)
                .Deserialize<HttpStatusCode>();
            if (statusCode == HttpStatusCode.NotFound)
                throw new NotFoundException("Id", id.ToString());
            else
            {
                throw new Exception();
            }
        }
    }

    [HttpPut]
    public async Task<IActionResult> UpdatePost(PostRequestToFullUpdate postRequestToFullUpdate)
    {
        var correlationId = Guid.NewGuid().ToString();
        var message = new Message<long, PostRequest>
        {
            Key = postRequestToFullUpdate.NewsId,
            Value = new PostRequest
            {
                CorrelationId = correlationId,
                OperationType = OperationType.Update,
                Payload = new PostRequestToFullUpdate()
                {
                    Id = postRequestToFullUpdate.Id,
                    NewsId = postRequestToFullUpdate.NewsId,
                    Content = postRequestToFullUpdate.Content,
                },
            },
        };

        var tcs = new TaskCompletionSource<PostResponse>();
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

        PostResponseToGetById responseResult = ((JsonElement)response.Result)
            .Deserialize<PostResponseToGetById>();
        
        var redisKey = $"{_cachePrefix}:{responseResult.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);
        
        return StatusCode(StatusCodes.Status200OK, responseResult);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeletePost(long id)
    {
        var correlationId = Guid.NewGuid().ToString();
        var message = new Message<long, PostRequest>
        {
            Key = 0,
            Value = new PostRequest()
            {
                CorrelationId = correlationId,
                OperationType = OperationType.Delete,
                Payload = new PostRequestToDeleteById() { Id = id },
            },
        };

        var tcs = new TaskCompletionSource<PostResponse>();
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
        try
        {
            var responseResult = ((JsonElement)response.Result).Deserialize<PostResponseToGetById>();
            var redisKey = $"{_cachePrefix}:{id}";
            await _redis.RemoveCacheValueAsync(redisKey);
            return StatusCode(StatusCodes.Status204NoContent);
        }
        catch
        {
            throw new NotFoundException("Id", id.ToString());
        }
    }   
}