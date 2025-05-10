using StackExchange.Redis;
using System.Threading.Tasks;

public class RedisCacheService
{
    private readonly IDatabase _redisDatabase;

    public RedisCacheService(IConnectionMultiplexer redis)
    {
        _redisDatabase = redis.GetDatabase(); 
    }

    public async Task<string> GetStringAsync(string key)
    {
        try
        {
            return await _redisDatabase.StringGetAsync(key);
        }
        catch (RedisException ex)
        {
            return null;
        }
    }

    public async Task SetStringAsync(string key, string value, int? expirySeconds = null)
    {
        try
        {
            if (expirySeconds.HasValue)
            {
                await _redisDatabase.StringSetAsync(key, value, TimeSpan.FromSeconds(expirySeconds.Value));
            }
            else
            {
                await _redisDatabase.StringSetAsync(key, value);
            }
        }
        catch (RedisException ex)
        {
        }
    }

    public async Task RemoveAsync(string key)
    {
        try
        {
            await _redisDatabase.KeyDeleteAsync(key);
        }
        catch (RedisException ex)
        {
        }
    }
}