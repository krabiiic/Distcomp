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
        
        var notes = await _innerClient.GetNotesAsync();
        if (notes != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(notes, _jsonOptions),
                new DistributedCacheEntryOptions
                {
                    AbsoluteExpirationRelativeToNow = _cacheDuration
                });
        }
        
        return notes;
    }

    public async Task<NoteResponseDTO?> GetNoteByIdAsync(long id)
    {
        string cacheKey = $"discussion:note:{id}";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        if (!string.IsNullOrEmpty(cachedData))
        {
            return JsonSerializer.Deserialize<NoteResponseDTO>(cachedData, _jsonOptions);
        }
        
        var note = await _innerClient.GetNoteByIdAsync(id);
        if (note != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(note, _jsonOptions),
                new DistributedCacheEntryOptions
                {
                    AbsoluteExpirationRelativeToNow = _cacheDuration
                });
        }
        
        return note;
    }

    public async Task<NoteResponseDTO?> CreateNoteAsync(NoteRequestDTO noteRequest)
    {
        var note = await _innerClient.CreateNoteAsync(noteRequest);
        await InvalidateCacheAsync(note.Id);
        return note;
    }

    public async Task<NoteResponseDTO?> UpdateNoteAsync(long id, NoteRequestDTO noteRequest)
    {
        var note = await _innerClient.UpdateNoteAsync(id, noteRequest);
        await InvalidateCacheAsync(note.Id);
        return note;
    }

    public async Task DeleteNoteAsync(long id)
    {
        await _innerClient.DeleteNoteAsync(id);
        await InvalidateCacheAsync(id);
    }

    private async Task InvalidateCacheAsync(long id)
    {
        await _cache.RemoveAsync("discussion:notes_all");
        await _cache.RemoveAsync($"discussion:note:{id}");
    }
}
