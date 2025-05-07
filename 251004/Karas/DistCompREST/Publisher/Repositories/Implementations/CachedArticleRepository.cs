// CachedArticleRepository.cs

using System.Text.Json;
using Microsoft.Extensions.Caching.Distributed;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class CachedArticleRepository : IArticleRepository
{
    private readonly IArticleRepository _decorated;
    private readonly IDistributedCache _cache;
    private readonly TimeSpan _cacheDuration = TimeSpan.FromMinutes(2);

    public CachedArticleRepository(IArticleRepository decorated, IDistributedCache cache)
    {
        _decorated = decorated;
        _cache = cache;
    }

    public async Task<IEnumerable<Article>> GetAllAsync()
    {
        const string cacheKey = "stories_all";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        
        if (!string.IsNullOrEmpty(cachedData))
            return JsonSerializer.Deserialize<IEnumerable<Article>>(cachedData);

        var stories = await _decorated.GetAllAsync();
        await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(stories), new DistributedCacheEntryOptions
        {
            AbsoluteExpirationRelativeToNow = _cacheDuration
        });
        
        return stories;
    }

    public async Task<Article?> GetByIdAsync(long id)
    {
        var cacheKey = $"article_{id}";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        
        if (!string.IsNullOrEmpty(cachedData))
            return JsonSerializer.Deserialize<Article>(cachedData);

        var article = await _decorated.GetByIdAsync(id);
        if (article != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(article), new DistributedCacheEntryOptions
            {
                AbsoluteExpirationRelativeToNow = _cacheDuration
            });
        }
        
        return article;
    }

    public async Task<Article> CreateAsync(Article entity)
    {
        var result = await _decorated.CreateAsync(entity);
        await InvalidateCacheForArticle(result.Id);
        return result;
    }

    public async Task<Article?> UpdateAsync(Article entity)
    {
        var result = await _decorated.UpdateAsync(entity);
        if (result != null)
            await InvalidateCacheForArticle(result.Id);
        
        return result;
    }

    public async Task<bool> DeleteAsync(long id)
    {
        var result = await _decorated.DeleteAsync(id);
        if (result)
            await InvalidateCacheForArticle(id);
        
        return result;
    }

    private async Task InvalidateCacheForArticle(long articleId)
    {
        await _cache.RemoveAsync($"article_{articleId}");
        await _cache.RemoveAsync("stories_all");
    }
}