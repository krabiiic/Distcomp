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
    
    public async Task<IEnumerable<MessageResponseDTO>?> GetMessagesAsync()
    {
        const string cacheKey = "discussion:messages_all";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        if (!string.IsNullOrEmpty(cachedData))
        {
            return JsonSerializer.Deserialize<IEnumerable<MessageResponseDTO>>(cachedData, _jsonOptions);
        }
        
        var messages = await _innerClient.GetMessagesAsync();
        if (messages != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(messages, _jsonOptions),
                new DistributedCacheEntryOptions
                {
                    AbsoluteExpirationRelativeToNow = _cacheDuration
                });
        }
        
        return messages;
    }

    public async Task<MessageResponseDTO?> GetMessageByIdAsync(long id)
    {
        string cacheKey = $"discussion:message:{id}";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        if (!string.IsNullOrEmpty(cachedData))
        {
            return JsonSerializer.Deserialize<MessageResponseDTO>(cachedData, _jsonOptions);
        }
        
        var message = await _innerClient.GetMessageByIdAsync(id);
        if (message != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(message, _jsonOptions),
                new DistributedCacheEntryOptions
                {
                    AbsoluteExpirationRelativeToNow = _cacheDuration
                });
        }
        
        return message;
    }

    public async Task<MessageResponseDTO?> CreateMessageAsync(MessageRequestDTO post)
    {
        var message = await _innerClient.CreateMessageAsync(post);
        await InvalidateCacheAsync(message.Id);
        return message;
    }

    public async Task<MessageResponseDTO?> UpdateMessageAsync(MessageRequestDTO post)
    {
        var message = await _innerClient.UpdateMessageAsync(post);
        await InvalidateCacheAsync(message.Id);
        return message;
    }

    public async Task DeleteMessageAsync(long id)
    {
        await _innerClient.DeleteMessageAsync(id);
        await InvalidateCacheAsync(id);
    }

    /// <summary>
    /// Инвалидирует кэш для списка и отдельного Notice.
    /// Если у вас есть дополнительные ключи, их тоже можно добавить.
    /// </summary>
    private async Task InvalidateCacheAsync(long id)
    {
        await _cache.RemoveAsync("discussion:messages_all");
        await _cache.RemoveAsync($"discussion:message:{id}");
    }
}
