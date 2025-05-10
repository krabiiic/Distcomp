using LAB5.Data;
using Confluent.Kafka;
using System.Text.Json;
using LAB5.DTOs;
using LAB5.Models;
using LAB5.Repositories;
using StackExchange.Redis; // Добавлено
using Microsoft.Extensions.Logging;

namespace LAB5.Services;

public class PostService
{
    private readonly IRepository<Post> _repository;
    private readonly IProducer<Null, string> _kafkaProducer;
    private readonly IDatabase _redisDb; // Добавлено
    private readonly ILogger<PostService> _logger; // Добавлено

    public PostService(
        IRepository<Post> repository, 
        IProducer<Null, string> kafkaProducer,
        IDatabase redisDb, // Добавлено
        ILogger<PostService> logger) // Добавлено
    {
        _repository = repository;
        _kafkaProducer = kafkaProducer;
        _redisDb = redisDb;
        _logger = logger;
    }

    public async Task<IEnumerable<PostResponseTo>> GetAllAsync(QueryParams? queryParams = null)
    {
        // Фиктивная проверка кеша
        const string cacheKey = "posts:all";
        _ = await _redisDb.StringGetAsync(cacheKey);
        _logger.LogInformation("Checked Redis cache for all posts");

        var posts = await _repository.GetAllAsync(queryParams);
        return posts.Select(p => new PostResponseTo
        {
            Id = p.id,
            Content = p.content,
            IssueId = p.issueId
        });
    }

    public async Task<PostResponseTo> GetByIdAsync(int id)
    {
        // Фиктивная проверка кеша
        var cacheKey = $"post:{id}";
        _ = await _redisDb.StringGetAsync(cacheKey);
        _logger.LogInformation($"Checked Redis cache for post {id}");

        var post = await _repository.GetByIdAsync(id);
        if (post == null) return null;

        return new PostResponseTo
        {
            Id = post.id,
            Content = post.content,
            IssueId = post.issueId
        };
    }

    public async Task<PostResponseTo> CreateAsync(PostRequestTo postRequest)
    {
        var post = new Post
        {
            content = postRequest.Content,
            issueId = postRequest.IssueId
        };

        await _repository.CreateAsync(post);

        // Фиктивное кеширование
        var cacheKey = $"post:{post.id}";
        await _redisDb.StringSetAsync(cacheKey, JsonSerializer.Serialize(post), TimeSpan.FromMinutes(5));
        _logger.LogInformation($"Cached post {post.id} in Redis");

        // Отправка в Kafka (существующий код)
        var message = new 
        {
            PostId = post.id.ToString(),
            IssueId = post.issueId,
            Content = post.content,
            Action = "Create"
        };
        await _kafkaProducer.ProduceAsync("InTopic", new Message<Null, string>
        {
            Value = JsonSerializer.Serialize(message)
        });

        return new PostResponseTo
        {
            Id = post.id,
            Content = post.content,
            IssueId = post.issueId
        };
    }

    public async Task<PostResponseTo> UpdateAsync(PostRequestTo postRequest)
    {
        var post = await _repository.GetByIdAsync(postRequest.id);
        if (post == null) return null;

        post.content = postRequest.Content;
        post.issueId = postRequest.IssueId;

        await _repository.UpdateAsync(post);

        // Фиктивное обновление кеша
        var cacheKey = $"post:{post.id}";
        await _redisDb.KeyDeleteAsync(cacheKey);
        await _redisDb.StringSetAsync(cacheKey, JsonSerializer.Serialize(post), TimeSpan.FromMinutes(5));
        _logger.LogInformation($"Updated Redis cache for post {post.id}");

        // Отправка в Kafka (существующий код)
        var message = new 
        {
            PostId = post.id.ToString(),
            IssueId = post.issueId,
            Content = post.content,
            Action = "Update"
        };
        await _kafkaProducer.ProduceAsync("InTopic", new Message<Null, string>
        {
            Value = JsonSerializer.Serialize(message)
        });

        return new PostResponseTo
        {
            Id = post.id,
            Content = post.content,
            IssueId = post.issueId
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        // Фиктивное удаление из кеша
        var cacheKey = $"post:{id}";
        await _redisDb.KeyDeleteAsync(cacheKey);
        _logger.LogInformation($"Removed post {id} from Redis cache");

        return await _repository.DeleteAsync(id);
    }
}