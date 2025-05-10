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
        await InvalidateCacheForUser(result.Id);
        return result;
    }

    public async Task<Creator?> UpdateAsync(Creator entity)
    {
        var result = await _decorated.UpdateAsync(entity);
        if (result != null)
        {
            await InvalidateCacheForUser(result.Id);
        }
        return result;
    }

    public async Task<bool> DeleteAsync(long id)
    {
        var result = await _decorated.DeleteAsync(id);
        if (result)
        {
            await InvalidateCacheForUser(id);
        }
        return result;
    }

    private async Task InvalidateCacheForUser(long userId)
    {
        await _cache.RemoveAsync($"creator_{userId}");
        await _cache.RemoveAsync("creators_all");
    }

    public async Task<bool> HasLogin(string creatorLogin)
    {
        string cacheKey = $"creator_haslogin_{creatorLogin}";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        if (!string.IsNullOrEmpty(cachedData))
        {
            if (bool.TryParse(cachedData, out bool cachedResult))
            {
                return cachedResult;
            }
        }
    
        bool result = await _decorated.HasLogin(creatorLogin);
        await _cache.SetStringAsync(
            cacheKey, 
            result.ToString(), 
            new DistributedCacheEntryOptions
            {
                AbsoluteExpirationRelativeToNow = _cacheDuration
            });
        
        return result;
    }

    public async Task<bool> HasUser(long creatorUserId)
    {
        string cacheKey = $"creator_hasuser_{creatorUserId}";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        if (!string.IsNullOrEmpty(cachedData))
        {
            if (bool.TryParse(cachedData, out bool cachedResult))
            {
                return cachedResult;
            }
        }
    
        bool result = await _decorated.HasUser(creatorUserId);
        await _cache.SetStringAsync(
            cacheKey, 
            result.ToString(), 
            new DistributedCacheEntryOptions
            {
                AbsoluteExpirationRelativeToNow = _cacheDuration
            });
        
        return result;
    }
}