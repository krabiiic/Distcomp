// CachedIssueRepository.cs

using System.Text.Json;
using Microsoft.Extensions.Caching.Distributed;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class CachedIssueRepository : IIssueRepository
{
    private readonly IIssueRepository _decorated;
    private readonly IDistributedCache _cache;
    private readonly TimeSpan _cacheDuration = TimeSpan.FromMinutes(2);

    public CachedIssueRepository(IIssueRepository decorated, IDistributedCache cache)
    {
        _decorated = decorated;
        _cache = cache;
    }

    public async Task<IEnumerable<Issue>> GetAllAsync()
    {
        const string cacheKey = "issues_all";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        
        if (!string.IsNullOrEmpty(cachedData))
            return JsonSerializer.Deserialize<IEnumerable<Issue>>(cachedData);

        var issues = await _decorated.GetAllAsync();
        await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(issues), new DistributedCacheEntryOptions
        {
            AbsoluteExpirationRelativeToNow = _cacheDuration
        });
        
        return issues;
    }

    public async Task<Issue?> GetByIdAsync(long id)
    {
        var cacheKey = $"issue_{id}";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        
        if (!string.IsNullOrEmpty(cachedData))
            return JsonSerializer.Deserialize<Issue>(cachedData);

        var issue = await _decorated.GetByIdAsync(id);
        if (issue != null)
        {
            await _cache.SetStringAsync(cacheKey, JsonSerializer.Serialize(issue), new DistributedCacheEntryOptions
            {
                AbsoluteExpirationRelativeToNow = _cacheDuration
            });
        }
        
        return issue;
    }

    public async Task<Issue> CreateAsync(Issue entity)
    {
        var result = await _decorated.CreateAsync(entity);
        await InvalidateCacheForStory(result.Id);
        return result;
    }

    public async Task<Issue?> UpdateAsync(Issue entity)
    {
        var result = await _decorated.UpdateAsync(entity);
        if (result != null)
            await InvalidateCacheForStory(result.Id);
        
        return result;
    }

    public async Task<bool> DeleteAsync(long id)
    {
        var result = await _decorated.DeleteAsync(id);
        if (result)
            await InvalidateCacheForStory(id);
        
        return result;
    }

    private async Task InvalidateCacheForStory(long storyId)
    {
        await _cache.RemoveAsync($"issue_{storyId}");
        await _cache.RemoveAsync("issues_all");
    }

    public async Task<bool> HasIssue(string issue)
    {
        string cacheKey = $"issue_has_{issue}";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        if (!string.IsNullOrEmpty(cachedData) && bool.TryParse(cachedData, out bool cachedResult))
        {
            return cachedResult;
        }
    
        bool result = await _decorated.HasIssue(issue);
        await _cache.SetStringAsync(
            cacheKey,
            result.ToString(),
            new DistributedCacheEntryOptions
            {
                AbsoluteExpirationRelativeToNow = _cacheDuration
            });
        
        return result;
    }

    public async Task<bool> HasIssue(long issueId)
    {
        string cacheKey = $"issue_has_{issueId}";
        var cachedData = await _cache.GetStringAsync(cacheKey);
        if (!string.IsNullOrEmpty(cachedData) && bool.TryParse(cachedData, out bool cachedResult))
        {
            return cachedResult;
        }
    
        bool result = await _decorated.HasIssue(issueId);
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