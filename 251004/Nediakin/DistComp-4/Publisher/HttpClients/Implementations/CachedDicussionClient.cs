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
    
    public async Task<IEnumerable<NoteResponseDTO>?> GetNotesAsync()
    {
        const string cacheKey = "discussion:notes_all";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        if (!string.IsNullOrEmpty(cachedData))
        {
            return JsonSerializer.Deserialize<IEnumerable<NoteResponseDTO>>(cachedData, _jsonOptions);
        }
        
        var notices = await _innerClient.GetNotesAsync();
        if (notices != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(notices, _jsonOptions),
                new DistributedCacheEntryOptions
                {
                    AbsoluteExpirationRelativeToNow = _cacheDuration
                });
        }
        
        return notices;
    }

    public async Task<NoteResponseDTO?> GetNoteByIdAsync(long id)
    {
        string cacheKey = $"discussion:note:{id}";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        if (!string.IsNullOrEmpty(cachedData))
        {
            return JsonSerializer.Deserialize<NoteResponseDTO>(cachedData, _jsonOptions);
        }
        
        var notice = await _innerClient.GetNoteByIdAsync(id);
        if (notice != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(notice, _jsonOptions),
                new DistributedCacheEntryOptions
                {
                    AbsoluteExpirationRelativeToNow = _cacheDuration
                });
        }
        
        return notice;
    }

    public async Task<NoteResponseDTO?> CreateNoteAsync(NoteRequestDTO post)
    {
        var notice = await _innerClient.CreateNoteAsync(post);
        await InvalidateCacheAsync(notice.Id);
        return notice;
    }

    public async Task<NoteResponseDTO?> UpdateNoteAsync(NoteRequestDTO post)
    {
        var notice = await _innerClient.UpdateNoteAsync(post);
        await InvalidateCacheAsync(notice.Id);
        return notice;
    }

    public async Task DeleteNoteAsync(long id)
    {
        await _innerClient.DeleteNoteAsync(id);
        await InvalidateCacheAsync(id);
    }

    /// <summary>
    /// Инвалидирует кэш для списка и отдельного Notice.
    /// Если у вас есть дополнительные ключи, их тоже можно добавить.
    /// </summary>
    private async Task InvalidateCacheAsync(long id)
    {
        await _cache.RemoveAsync("discussion:notes_all");
        await _cache.RemoveAsync($"discussion:note:{id}");
    }
}
