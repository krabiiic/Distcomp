namespace LAB2.Services;

using DTOs;
using Repositories;
using Models;


public class PostService
{
    private readonly IRepository<Post> _repository;

    public PostService(IRepository<Post> repository)
    {
        _repository = repository;
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
            throw new ArgumentException("Content must be less than 2048 characters.");
        }

        var post = new Post
        {
            content = postRequest.Content,
            issueId = postRequest.IssueId
        };

        await _repository.CreateAsync(post);
        return new PostResponseTo
        {
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
            throw new ArgumentException("Content must be less than 2048 characters.");
        }

        var post = await _repository.GetByIdAsync(postRequest.id);
        if (post == null) return null;

        post.content = postRequest.Content;
        post.issueId = postRequest.IssueId;

        await _repository.UpdateAsync(post);
        return new PostResponseTo
        {
            Id = post.id,
            Content = post.content,
            IssueId = post.issueId
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        return await _repository.DeleteAsync(id);
    }
}