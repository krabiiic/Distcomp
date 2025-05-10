// CachedLabelRepository.cs

using System.Text.Json;
using Microsoft.Extensions.Caching.Distributed;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class CachedLabelRepository : ILabelRepository
{
    private readonly ILabelRepository _decorated;
    private readonly IDistributedCache _cache;
    private readonly TimeSpan _cacheDuration = TimeSpan.FromMinutes(2);

    public CachedLabelRepository(ILabelRepository decorated, IDistributedCache cache)
    {
        _decorated = decorated;
        _cache = cache;
    }

    public async Task<IEnumerable<Label>> GetAllAsync()
    {
        const string cacheKey = "labels_all";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        
        if (!string.IsNullOrEmpty(cachedData))
            return JsonSerializer.Deserialize<IEnumerable<Label>>(cachedData);

        var labels = await _decorated.GetAllAsync();
        await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(labels), new DistributedCacheEntryOptions
        {
            AbsoluteExpirationRelativeToNow = _cacheDuration
        });
        
        return labels;
    }

    public async Task<Label?> GetByIdAsync(long id)
    {
        var cacheKey = $"label_{id}";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        
        if (!string.IsNullOrEmpty(cachedData))
            return JsonSerializer.Deserialize<Label>(cachedData);

        var label = await _decorated.GetByIdAsync(id);
        if (label != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(label), new DistributedCacheEntryOptions
            {
                AbsoluteExpirationRelativeToNow = _cacheDuration
            });
        }
        
        return label;
    }

    public async Task<Label> CreateAsync(Label entity)
    {
        var result = await _decorated.CreateAsync(entity);
        await InvalidateCacheForLabel(result.Id);
        return result;
    }

    public async Task<Label?> UpdateAsync(Label entity)
    {
        var result = await _decorated.UpdateAsync(entity);
        if (result != null)
            await InvalidateCacheForLabel(result.Id);
        
        return result;
    }

    public async Task<bool> DeleteAsync(long id)
    {
        var result = await _decorated.DeleteAsync(id);
        if (result)
            await InvalidateCacheForLabel(id);
        
        return result;
    }

    private async Task InvalidateCacheForLabel(long labelId)
    {
        await _cache.RemoveAsync($"label_{labelId}");
        await _cache.RemoveAsync("labels_all");
    }
}