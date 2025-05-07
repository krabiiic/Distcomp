// CachedMarkRepository.cs

using System.Text.Json;
using Microsoft.Extensions.Caching.Distributed;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class CachedMarkRepository : IMarkRepository
{
    private readonly IMarkRepository _decorated;
    private readonly IDistributedCache _cache;
    private readonly TimeSpan _cacheDuration = TimeSpan.FromMinutes(2);

    public CachedMarkRepository(IMarkRepository decorated, IDistributedCache cache)
    {
        _decorated = decorated;
        _cache = cache;
    }

    public async Task<IEnumerable<Mark>> GetAllAsync()
    {
        const string cacheKey = "marks_all";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        
        if (!string.IsNullOrEmpty(cachedData))
            return JsonSerializer.Deserialize<IEnumerable<Mark>>(cachedData);

        var marks = await _decorated.GetAllAsync();
        await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(marks), new DistributedCacheEntryOptions
        {
            AbsoluteExpirationRelativeToNow = _cacheDuration
        });
        
        return marks;
    }

    public async Task<Mark?> GetByIdAsync(long id)
    {
        var cacheKey = $"mark_{id}";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        
        if (!string.IsNullOrEmpty(cachedData))
            return JsonSerializer.Deserialize<Mark>(cachedData);

        var mark = await _decorated.GetByIdAsync(id);
        if (mark != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(mark), new DistributedCacheEntryOptions
            {
                AbsoluteExpirationRelativeToNow = _cacheDuration
            });
        }
        
        return mark;
    }

    public async Task<Mark> CreateAsync(Mark entity)
    {
        var result = await _decorated.CreateAsync(entity);
        await InvalidateCacheForMark(result.Id);
        return result;
    }

    public async Task<Mark?> UpdateAsync(Mark entity)
    {
        var result = await _decorated.UpdateAsync(entity);
        if (result != null)
            await InvalidateCacheForMark(result.Id);
        
        return result;
    }

    public async Task<bool> DeleteAsync(long id)
    {
        var result = await _decorated.DeleteAsync(id);
        if (result)
            await InvalidateCacheForMark(id);
        
        return result;
    }

    private async Task InvalidateCacheForMark(long markId)
    {
        await _cache.RemoveAsync($"mark_{markId}");
        await _cache.RemoveAsync("marks_all");
    }
}