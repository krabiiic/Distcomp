using System.Collections.Concurrent;

namespace CassandraPosts.Services;

public class IdMappingService
{
    private readonly ConcurrentDictionary<string, Guid> _mapping = new();
    private readonly ILogger<IdMappingService> _logger;

    public IdMappingService(ILogger<IdMappingService> logger)
    {
        _logger = logger;
    }

    public void AddMapping(string postId, Guid cassandraId)
    {
        _mapping[postId] = cassandraId;
        _logger.LogInformation($"Added mapping: {postId} -> {cassandraId}");
    }

    public bool TryGetMapping(string postId, out Guid cassandraId)
    {
        var result = _mapping.TryGetValue(postId, out cassandraId);
        _logger.LogInformation($"Try get mapping: {postId} -> {result} ({cassandraId})");
        return result;
    }

    public void RemoveMapping(string postId)
    {
        _mapping.TryRemove(postId, out _);
        _logger.LogInformation($"Removed mapping: {postId}");
    }

    public void DebugLogMappings()
    {
        _logger.LogInformation("Current mappings:");
        foreach (var mapping in _mapping)
        {
            _logger.LogInformation($"{mapping.Key} -> {mapping.Value}");
        }
    }
}