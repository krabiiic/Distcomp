using System.Text.Json;
using Microsoft.Extensions.Caching.Distributed;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.HttpClients.Interfaces;

namespace Publisher.HttpClients.Implementations;

public class CachedDiscussionClient : IDiscussionClient
{
    private readonly IDiscussionClient _innerClient;
    private readonly IDistributedCache _cache;
    private readonly JsonSerializerOptions _jsonOptions = new() { PropertyNameCaseInsensitive = true };
    private readonly TimeSpan _cacheDuration = TimeSpan.FromMinutes(2);

    public CachedDiscussionClient(IDiscussionClient innerClient, IDistributedCache cache)
    {
        _innerClient = innerClient;
        _cache = cache;
    }
    
    public async Task<IEnumerable<PostResponseDTO>?> GetPostsAsync()
    {
        const string cacheKey = "discussion:posts_all";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        if (!string.IsNullOrEmpty(cachedData))
        {
            return JsonSerializer.Deserialize<IEnumerable<PostResponseDTO>>(cachedData, _jsonOptions);
        }
        
        var posts = await _innerClient.GetPostsAsync();
        if (posts != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(posts, _jsonOptions),
                new DistributedCacheEntryOptions
                {
                    AbsoluteExpirationRelativeToNow = _cacheDuration
                });
        }
        
        return posts;
    }

    public async Task<PostResponseDTO?> GetPostByIdAsync(long id)
    {
        string cacheKey = $"discussion:post:{id}";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        if (!string.IsNullOrEmpty(cachedData))
        {
            return JsonSerializer.Deserialize<PostResponseDTO>(cachedData, _jsonOptions);
        }
        
        var post = await _innerClient.GetPostByIdAsync(id);
        if (post != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(post, _jsonOptions),
                new DistributedCacheEntryOptions
                {
                    AbsoluteExpirationRelativeToNow = _cacheDuration
                });
        }
        
        return post;
    }

    public async Task<PostResponseDTO?> CreatePostAsync(PostRequestDTO postRequest)
    {
        var post = await _innerClient.CreatePostAsync(postRequest);
        await InvalidateCacheAsync(post.Id);
        return post;
    }

    public async Task<PostResponseDTO?> UpdatePostAsync(PostRequestDTO postRequest)
    {
        var post = await _innerClient.UpdatePostAsync(postRequest);
        await InvalidateCacheAsync(post.Id);
        return post;
    }

    public async Task DeletePostAsync(long id)
    {
        await _innerClient.DeletePostAsync(id);
        await InvalidateCacheAsync(id);
    }

    private async Task InvalidateCacheAsync(long id)
    {
        await _cache.RemoveAsync("discussion:posts_all");
        await _cache.RemoveAsync($"discussion:post:{id}");
    }
}
