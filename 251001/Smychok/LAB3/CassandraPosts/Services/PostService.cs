using Cassandra.Mapping;
using CassandraPosts.Models;
using CassandraPosts.Repositories;

namespace CassandraPosts.Services;

public class PostService
{
    private readonly IPostRepository _repository;

    public PostService(IPostRepository repository)
    {
        _repository = repository;
    }

    public async Task<Post> CreateAsync(Post post)
    {
        post.Id = Guid.NewGuid();
        return await _repository.CreateAsync(post);
    }

    public async Task<Post?> GetAsync(string country, long issueId, long id)
    {
        return await _repository.GetAsync(country, issueId, id);
    }

    public async Task<Post?> GetByIdAsync(Guid postId)
    {
        return await _repository.GetByIdAsync(postId);
    }

    public async Task<IPage<Post>> GetAllAsync(int pageSize = 10, byte[]? pagingState = null)
    {
        return await _repository.GetAllAsync(pageSize, pagingState);
    }

    public async Task<Post> UpdateAsync(Post post)
    {
        return await _repository.UpdateAsync(post);
    }

    public async Task<bool> DeleteAsync(string country, long issueId, long id)
    {
        return await _repository.DeleteAsync(country, issueId, id);
    }

    public async Task<bool> DeleteAsync(Guid postId)
    {
        return await _repository.DeleteAsync(postId);
    }
}