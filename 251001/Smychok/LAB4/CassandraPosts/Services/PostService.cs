using Cassandra.Mapping;
using CassandraPosts.Models;
using CassandraPosts.Repositories;

namespace CassandraPosts.Services;

public class PostService
{
    private readonly IPostRepository _repository;
    private readonly ILogger<PostService> _logger;
    
    public PostService(IPostRepository repository, ILogger<PostService> logger)
    {
        _repository = repository;
        _logger = logger;
    }

    public async Task<Post> CreateAsync(Post post)
    {
        try
        {
            post.id = Guid.NewGuid();
            // Устанавливаем состояние по умолчанию
            post.State = post.State ?? "PENDING";
            return await _repository.CreateAsync(post);
        }
        catch (Exception ex)
        {
            _logger.LogError($"Error creating post: {ex.Message}");
            throw;
        }
    }
    

    public async Task<Post?> GetByIdAsync(Guid postId)
    {
         return await _repository.GetByIdAsync(postId);
    }

    public async Task<IEnumerable<Post>> GetAllAsync(int pageSize = 10, byte[]? pagingState = null)
    {
        return await _repository.GetAllAsync();
    }

    public async Task<Post> UpdateAsync(Post post)
    {
        try
        {
            // Сохраняем состояние при обновлении
            var existing = await _repository.GetByIdAsync(post.id);
            if (existing != null)
            {
                post.State = existing.State;
            }
            return await _repository.UpdateAsync(post);
        }
        catch (Exception ex)
        {
            _logger.LogError($"Error updating post: {ex.Message}");
            throw;
        }
    }

    public async Task<bool> DeleteAsync(Guid postId)
    {
        try
        {
            var post = await _repository.GetByIdAsync(postId);
            if (post == null) return false;

            await _repository.DeleteAsync(postId);
            return true;
        }
        catch (Exception ex)
        {
            _logger.LogError($"Error deleting post: {ex.Message}");
            return false;
        }
    }
}