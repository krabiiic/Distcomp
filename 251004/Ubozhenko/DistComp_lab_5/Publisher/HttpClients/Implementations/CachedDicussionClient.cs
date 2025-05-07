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
    
    public async Task<IEnumerable<ReactionResponseDTO>?> GetReactionsAsync()
    {
        const string cacheKey = "discussion:reactions_all";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        if (!string.IsNullOrEmpty(cachedData))
        {
            return JsonSerializer.Deserialize<IEnumerable<ReactionResponseDTO>>(cachedData, _jsonOptions);
        }
        
        var reactions = await _innerClient.GetReactionsAsync();
        if (reactions != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(reactions, _jsonOptions),
                new DistributedCacheEntryOptions
                {
                    AbsoluteExpirationRelativeToNow = _cacheDuration
                });
        }
        
        return reactions;
    }

    public async Task<ReactionResponseDTO?> GetReactionByIdAsync(long id)
    {
        string cacheKey = $"discussion:reaction:{id}";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        if (!string.IsNullOrEmpty(cachedData))
        {
            return JsonSerializer.Deserialize<ReactionResponseDTO>(cachedData, _jsonOptions);
        }
        
        var reaction = await _innerClient.GetReactionByIdAsync(id);
        if (reaction != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(reaction, _jsonOptions),
                new DistributedCacheEntryOptions
                {
                    AbsoluteExpirationRelativeToNow = _cacheDuration
                });
        }
        
        return reaction;
    }

    public async Task<ReactionResponseDTO?> CreateReactionAsync(ReactionRequestDTO post)
    {
        var reaction = await _innerClient.CreateReactionAsync(post);
        await InvalidateCacheAsync(reaction.Id);
        return reaction;
    }

    public async Task<ReactionResponseDTO?> UpdateReactionAsync(ReactionRequestDTO post)
    {
        var reaction = await _innerClient.UpdateReactionAsync(post);
        await InvalidateCacheAsync(reaction.Id);
        return reaction;
    }

    public async Task DeleteReactionAsync(long id)
    {
        await _innerClient.DeleteReactionAsync(id);
        await InvalidateCacheAsync(id);
    }

    /// <summary>
    /// Инвалидирует кэш для списка и отдельного Reaction.
    /// Если у вас есть дополнительные ключи, их тоже можно добавить.
    /// </summary>
    private async Task InvalidateCacheAsync(long id)
    {
        await _cache.RemoveAsync("discussion:reactions_all");
        await _cache.RemoveAsync($"discussion:reaction:{id}");
    }
}
