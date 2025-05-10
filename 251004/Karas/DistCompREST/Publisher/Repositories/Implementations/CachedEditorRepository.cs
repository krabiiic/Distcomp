using System.Text.Json;
using Microsoft.Extensions.Caching.Distributed;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class CachedEditorRepository : IEditorRepository
{
    private readonly IEditorRepository _decorated;
    private readonly IDistributedCache _cache;
    private readonly TimeSpan _cacheDuration = TimeSpan.FromMinutes(2);

    public CachedEditorRepository(IEditorRepository decorated, IDistributedCache cache)
    {
        _decorated = decorated;
        _cache = cache;
    }

    public async Task<IEnumerable<Editor>> GetAllAsync()
    {
        const string cacheKey = "editors_all";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        
        if (!string.IsNullOrEmpty(cachedData))
        {
            return JsonSerializer.Deserialize<IEnumerable<Editor>>(cachedData);
        }

        var editors = await _decorated.GetAllAsync();
        await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(editors), new DistributedCacheEntryOptions
        {
            AbsoluteExpirationRelativeToNow = _cacheDuration
        });
        
        return editors;
    }

    public async Task<Editor?> GetByIdAsync(long id)
    {
        string cacheKey = $"editor_{id}";
        Console.WriteLine(cacheKey);
        var cachedData = await _cache.GetStringAsync(cacheKey);
        
        if (!string.IsNullOrEmpty(cachedData))
        {
            return JsonSerializer.Deserialize<Editor>(cachedData);
        }

        var editor = await _decorated.GetByIdAsync(id);
        if (editor != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(editor), new DistributedCacheEntryOptions
            {
                AbsoluteExpirationRelativeToNow = _cacheDuration
            });
        }
        
        return editor;
    }

    // Остальные методы с инвалидацией кэша
    public async Task<Editor> CreateAsync(Editor entity)
    {
        var result = await _decorated.CreateAsync(entity);
        await InvalidateCacheForEditor(result.Id);
        return result;
    }

    public async Task<Editor?> UpdateAsync(Editor entity)
    {
        var result = await _decorated.UpdateAsync(entity);
        if (result != null)
        {
            await InvalidateCacheForEditor(result.Id);
        }
        return result;
    }

    public async Task<bool> DeleteAsync(long id)
    {
        var result = await _decorated.DeleteAsync(id);
        if (result)
        {
            await InvalidateCacheForEditor(id);
        }
        return result;
    }

    private async Task InvalidateCacheForEditor(long editorId)
    {
        await _cache.RemoveAsync($"editor_{editorId}");
        await _cache.RemoveAsync("editors_all");
    }
}