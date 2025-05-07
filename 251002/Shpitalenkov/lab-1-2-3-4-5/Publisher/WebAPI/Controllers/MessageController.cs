using System.Net;
using System.Text.Json;
using Core.DTO;
using Azure;
using Confluent.Kafka;
using Core.Exceptions;
using Core.Kafka;
using Core.Repositories.Redis;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using WebApi.Settings;

namespace WebApi.Controllers;

[ApiController]
[Route("/api/v1.0/messages")]
public class MessageController : ControllerBase
{
    private readonly IRedisCacheService _redis;
    private readonly KafkaPublisher<long, MessageRequest> _kafkaPublisher;
    private readonly KafkaResponseDispatcher<MessageResponse> _dispatcher;

    private readonly string _cachePrefix = "message";
    private readonly TimeSpan _cacheExpiration = TimeSpan.FromSeconds(10);
    
    private long _id = 1;

    private long getNextId()
    {
        return _id++;
    }
    
    public MessageController(
        KafkaPublisher<long, MessageRequest> kafkaPublisher,
        KafkaResponseDispatcher<MessageResponse> dispatcher,
        IRedisCacheService redis
    )
    {
        _kafkaPublisher = kafkaPublisher;
        _dispatcher = dispatcher;
        _redis = redis;
    }

    [HttpPost]
    public async Task<IActionResult> CreateMessage(MessageRequestToCreate messageRequestToCreate)
    {
        var correlationId = Guid.NewGuid().ToString();
        var nextId = getNextId();
        var message = new Message<long, MessageRequest>
        {
            Key = messageRequestToCreate.ArticleId,
            Value = new MessageRequest()
            {
                CorrelationId = correlationId,
                OperationType = OperationType.Create,
                Payload = new MessageRequestToFullUpdate()
                {
                    Id = nextId,
                    ArticleId = messageRequestToCreate.ArticleId,
                    Content = messageRequestToCreate.Content,
                },
            },
        };

        await _kafkaPublisher.Publish(
            new[] { (message.Key, message.Value) },
            CancellationToken.None
        );

        return StatusCode(
            StatusCodes.Status201Created,
            new MessageResponseToGetById()
            {
                Id = nextId,
                ArticleId = messageRequestToCreate.ArticleId,
                Content = messageRequestToCreate.Content,
            }
        );
    }

    [HttpGet]
    public async Task<IActionResult> GetMesages()
    {
        var correlationId = Guid.NewGuid().ToString();
        var message = new Message<long, MessageRequest>
        {
            Key = 0,
            Value = new MessageRequest
            {
                CorrelationId = correlationId,
                OperationType = OperationType.GetAll,
                Payload = new MessageRequestToGetAll(),
            },
        };

        var tcs = new TaskCompletionSource<MessageResponse>();
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
            .Deserialize<IEnumerable<MessageResponseToGetById?>?>();
        

        return StatusCode(StatusCodes.Status200OK, responseResult);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetMessageByIdQuery(long id)
    {
        var cachedResult = await _redis.GetCacheValueAsync<MessageResponseToGetById?>(
            $"{_cachePrefix}:{id}"
        );

        if (cachedResult is not null)
        {
            return StatusCode(StatusCodes.Status200OK, cachedResult);
        }
        
        var correlationId = Guid.NewGuid().ToString();
        var message = new Message<long, MessageRequest>
        {
            Key = 0,
            Value = new MessageRequest
            {
                CorrelationId = correlationId,
                OperationType = OperationType.GetById,
                Payload = new MessageRequestToGetById(){Id = id},
            },
        };

        var tcs = new TaskCompletionSource<MessageResponse>();
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
            MessageResponseToGetById responseResult = ((JsonElement)response.Result)
                .Deserialize<MessageResponseToGetById>();
            
            await _redis.SetCacheValueAsync<MessageResponseToGetById>(
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
    public async Task<IActionResult> UpdateMessage(MessageRequestToFullUpdate messageRequestToFullUpdate)
    {
        var correlationId = Guid.NewGuid().ToString();
        var message = new Message<long, MessageRequest>
        {
            Key = messageRequestToFullUpdate.ArticleId,
            Value = new MessageRequest
            {
                CorrelationId = correlationId,
                OperationType = OperationType.Update,
                Payload = new MessageRequestToFullUpdate()
                {
                    Id = messageRequestToFullUpdate.Id,
                    ArticleId = messageRequestToFullUpdate.ArticleId,
                    Content = messageRequestToFullUpdate.Content,
                },
            },
        };

        var tcs = new TaskCompletionSource<MessageResponse>();
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

        MessageResponseToGetById responseResult = ((JsonElement)response.Result)
            .Deserialize<MessageResponseToGetById>();
        
        var redisKey = $"{_cachePrefix}:{responseResult.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);
        
        return StatusCode(StatusCodes.Status200OK, responseResult);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteMessage(long id)
    {
        var correlationId = Guid.NewGuid().ToString();
        var message = new Message<long, MessageRequest>
        {
            Key = 0,
            Value = new MessageRequest()
            {
                CorrelationId = correlationId,
                OperationType = OperationType.Delete,
                Payload = new MessageRequestToDeleteById() { Id = id },
            },
        };

        var tcs = new TaskCompletionSource<MessageResponse>();
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
            var responseResult = ((JsonElement)response.Result).Deserialize<MessageResponseToGetById>();
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