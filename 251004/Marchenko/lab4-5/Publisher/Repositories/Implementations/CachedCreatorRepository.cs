using System.Text.Json;
using Microsoft.Extensions.Caching.Distributed;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class CachedCreatorRepository : ICreatorRepository
{
    private readonly ICreatorRepository _decorated;
    private readonly IDistributedCache _cache;
    private readonly TimeSpan _cacheDuration = TimeSpan.FromMinutes(2);

    public CachedCreatorRepository(ICreatorRepository decorated, IDistributedCache cache)
    {
        _decorated = decorated;
        _cache = cache;
    }

    public async Task<IEnumerable<Creator>> GetAllAsync()
    {
        const string cacheKey = "creators_all";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        
        if (!string.IsNullOrEmpty(cachedData))
        {
            return JsonSerializer.Deserialize<IEnumerable<Creator>>(cachedData);
        }

        var creators = await _decorated.GetAllAsync();
        await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(creators), new DistributedCacheEntryOptions
        {
            AbsoluteExpirationRelativeToNow = _cacheDuration
        });
        
        return creators;
    }

    public async Task<Creator?> GetByIdAsync(long id)
    {
        string cacheKey = $"creator_{id}";
        Console.WriteLine(cacheKey);
        var cachedData = await _cache.GetStringAsync(cacheKey);
        
        if (!string.IsNullOrEmpty(cachedData))
        {
            return JsonSerializer.Deserialize<Creator>(cachedData);
        }

        var creator = await _decorated.GetByIdAsync(id);
        if (creator != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(creator), new DistributedCacheEntryOptions
            {
                AbsoluteExpirationRelativeToNow = _cacheDuration
            });
        }
        
        return creator;
    }

    // Остальные методы с инвалидацией кэша
    public async Task<Creator> CreateAsync(Creator entity)
    {
        var result = await _decorated.CreateAsync(entity);
        await InvalidateCacheForCreator(result.Id);
        return result;
    }

    public async Task<Creator?> UpdateAsync(Creator entity)
    {
        var result = await _decorated.UpdateAsync(entity);
        if (result != null)
        {
            await InvalidateCacheForCreator(result.Id);
        }
        return result;
    }

    public async Task<bool> DeleteAsync(long id)
    {
        var result = await _decorated.DeleteAsync(id);
        if (result)
        {
            await InvalidateCacheForCreator(id);
        }
        return result;
    }

    private async Task InvalidateCacheForCreator(long creatorId)
    {
        await _cache.RemoveAsync($"creator_{creatorId}");
        await _cache.RemoveAsync("creators_all");
    }
}