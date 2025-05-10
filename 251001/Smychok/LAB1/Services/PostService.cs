namespace LAB1.Services;

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

    public async Task<IEnumerable<PostResponseTo>> GetAllAsync()
    {
        var posts = await _repository.GetAllAsync();
        return posts.Select(p => new PostResponseTo
        {
            Id = p.Id,
            Content = p.Content,
            IssueId = p.IssueId
        });
    }

    public async Task<PostResponseTo> GetByIdAsync(int id)
    {
        var post = await _repository.GetByIdAsync(id);
        if (post == null) return null;

        return new PostResponseTo
        {
            Id = post.Id,
            Content = post.Content,
            IssueId = post.IssueId
        };
    }

    public async Task<PostResponseTo> CreateAsync(PostRequestTo postRequest)
    {
        var post = new Post
        {
            Content = postRequest.Content,
            IssueId = postRequest.IssueId
        };

        await _repository.CreateAsync(post);
        return new PostResponseTo
        {
            Id = post.Id,
            Content = post.Content,
            IssueId = post.IssueId
        };
    }

    public async Task<PostResponseTo> UpdateAsync(int id, PostRequestTo postRequest)
    {
        var post = await _repository.GetByIdAsync(id);
        if (post == null) return null;

        post.Content = postRequest.Content;
        post.IssueId = postRequest.IssueId;

        await _repository.UpdateAsync(post);
        return new PostResponseTo
        {
            Id = post.Id,
            Content = post.Content,
            IssueId = post.IssueId
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        return await _repository.DeleteAsync(id);
    }
}