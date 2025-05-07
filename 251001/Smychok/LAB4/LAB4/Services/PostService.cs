using LAB4.Data;
using LAB4.DTOs;
using LAB4.Models;
using LAB4.Repositories;
using Confluent.Kafka;
using System.Text.Json;

namespace LAB4.Services;

public class PostService
{
    private readonly IRepository<Post> _repository;
    private readonly IProducer<Null, string> _kafkaProducer;

    public PostService(IRepository<Post> repository, IProducer<Null, string> kafkaProducer)
    {
        _repository = repository;
        _kafkaProducer = kafkaProducer;
    }

    public async Task<IEnumerable<PostResponseTo>> GetAllAsync(QueryParams? queryParams = null)
    {
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
        if (string.IsNullOrEmpty(postRequest.Content))
        {
            throw new ArgumentException("Content cannot be empty.");
        }

        if (postRequest.Content.Length > 2048)
        {
            throw new ArgumentException("Content must be less than 2048 characters.");
        }

        if (postRequest.Content.Length < 2)
        {
            throw new ArgumentException("Content must be at least 2 characters.");
        }

        var post = new Post
        {
            content = postRequest.Content,
            issueId = postRequest.IssueId
        };

        await _repository.CreateAsync(post);

        // Отправляем в Kafka как строку
        var message = new {
            PostId = post.id.ToString(), // Преобразуем в строку
            IssueId = post.issueId,
            Content = post.content,
            Action = "Create"
        };
    
        await _kafkaProducer.ProduceAsync("InTopic", new Message<Null, string> {
            Value = JsonSerializer.Serialize(message)
        });

        return new PostResponseTo {
            Id = post.id,
            Content = post.content,
            IssueId = post.issueId
        };
    }

    public async Task<PostResponseTo> UpdateAsync(PostRequestTo postRequest)
    {
        if (string.IsNullOrEmpty(postRequest.Content))
        {
            throw new ArgumentException("Content cannot be empty.");
        }

        if (postRequest.Content.Length > 2048)
        {
            throw new ArgumentException("Content must be less than 2048 characters.");
        }

        if (postRequest.Content.Length < 2)
        {
            throw new ArgumentException("Content must be at least 2 characters.");
        }

        var post = await _repository.GetByIdAsync(postRequest.id);
        if (post == null) return null;

        post.content = postRequest.Content;
        post.issueId = postRequest.IssueId;

        await _repository.UpdateAsync(post);

        // Отправка сообщения в Kafka
        var message = new {
            PostId = post.id.ToString(), // Преобразуем в строку
            IssueId = post.issueId,
            Content = post.content,
            Action = "Update" // или "Delete"
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
        var post = await _repository.GetByIdAsync(id);
        if (post == null) return false;

        // Отправка сообщения в Kafka
        var message = new {
            PostId = id.ToString(), // Преобразуем в строку
            IssueId = post.issueId,
            Content = post.content,
            Action = "Delete" // или "Delete"
        };
        await _kafkaProducer.ProduceAsync("InTopic", new Message<Null, string>
        {
            Value = JsonSerializer.Serialize(message)
        });

        return await _repository.DeleteAsync(id);
    }
}