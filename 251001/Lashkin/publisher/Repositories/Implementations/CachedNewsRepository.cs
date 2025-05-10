using System.Text.Json;
using Microsoft.Extensions.Caching.Distributed;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class CachedNewsRepository : INewsRepository
{
    private readonly INewsRepository _decorated;
    private readonly IDistributedCache _cache;
    private readonly TimeSpan _cacheDuration = TimeSpan.FromMinutes(2);

    public CachedNewsRepository(INewsRepository decorated, IDistributedCache cache)
    {
        _decorated = decorated;
        _cache = cache;
    }

    public async Task<IEnumerable<News>> GetAllAsync()
    {
        const string cacheKey = "stories_all";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        
        if (!string.IsNullOrEmpty(cachedData))
            return JsonSerializer.Deserialize<IEnumerable<News>>(cachedData);

        var stories = await _decorated.GetAllAsync();
        await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(stories), new DistributedCacheEntryOptions
        {
            AbsoluteExpirationRelativeToNow = _cacheDuration
        });
        
        return stories;
    }

    public async Task<News?> GetByIdAsync(long id)
    {
        var cacheKey = $"story_{id}";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        
        if (!string.IsNullOrEmpty(cachedData))
            return JsonSerializer.Deserialize<News>(cachedData);

        var story = await _decorated.GetByIdAsync(id);
        if (story != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(story), new DistributedCacheEntryOptions
            {
                AbsoluteExpirationRelativeToNow = _cacheDuration
            });
        }
        
        return story;
    }

    public async Task<News> CreateAsync(News entity)
    {
        var result = await _decorated.CreateAsync(entity);
        await InvalidateCacheForStory(result.Id);
        return result;
    }

    public async Task<News?> UpdateAsync(News entity)
    {
        var result = await _decorated.UpdateAsync(entity);
        if (result != null)
            await InvalidateCacheForStory(result.Id);
        
        return result;
    }

    public async Task<bool> DeleteAsync(long id)
    {
        var result = await _decorated.DeleteAsync(id);
        if (result)
            await InvalidateCacheForStory(id);
        
        return result;
    }

    private async Task InvalidateCacheForStory(long storyId)
    {
        await _cache.RemoveAsync($"story_{storyId}");
        await _cache.RemoveAsync("stories_all");
    }
}