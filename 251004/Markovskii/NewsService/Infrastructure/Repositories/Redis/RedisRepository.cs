using System.Text.Json;
using Microsoft.Extensions.Options;
using StackExchange.Redis;

namespace Infrastructure.Repositories.Redis;

public interface IRedisCacheService
{
    Task SetCacheValueAsync<T>(string key, T value, TimeSpan expiration);
    Task<T> GetCacheValueAsync<T>(string key);
    Task RemoveCacheValueAsync(string key);
}

public class RedisCacheService : IRedisCacheService, IRedisRepository
{
    private static ConnectionMultiplexer? _connection;
    private readonly RedisSettings _redisSettings;

    public RedisCacheService(IOptions<RedisSettings> redisOptions)
    {
        _redisSettings = redisOptions.Value;
    }

    protected async Task<IDatabase> GetConnection()
    {
        _connection ??= await ConnectionMultiplexer.ConnectAsync(_redisSettings.ConnectionString);

        return _connection.GetDatabase();
    }

    public async Task SetCacheValueAsync<T>(string key, T value, TimeSpan expiration)
    {
        var db = await GetConnection();
        var json = JsonSerializer.Serialize(value);
        await db.StringSetAsync(key, json, expiration);
    }

    public async Task<T> GetCacheValueAsync<T>(string key)
    {
        var db = await GetConnection();
        var json = await db.StringGetAsync(key);
        return json.HasValue ? JsonSerializer.Deserialize<T>(json) : default;
    }

    public async Task RemoveCacheValueAsync(string key)
    {
        var db = await GetConnection();
        await db.KeyDeleteAsync(key);
    }
}