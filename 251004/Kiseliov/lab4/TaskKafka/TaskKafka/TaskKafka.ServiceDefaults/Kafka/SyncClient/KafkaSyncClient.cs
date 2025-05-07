using System.Collections.Concurrent;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using TaskKafka.ServiceDefaults.Kafka.Producer;
namespace TaskKafka.ServiceDefaults.Kafka.SyncClient;

public sealed class KafkaSyncClient<TInput, TOutput>(
    KafkaMessageProducer<string, TInput> messageProducer,
    IOptions<KafkaSyncClientSetting<TInput, TOutput>> options,
    ILogger<KafkaSyncClient<TInput, TOutput>> logger) : IDisposable
    where TInput : class
    where TOutput : class
{
    private readonly TimeSpan _timeout = options.Value.Timeout;

    private readonly ConcurrentDictionary<string, TaskCompletionSource<TOutput>> _tasks = new();

    public async Task<TOutput> ProduceAsync(TInput value)
    {
        var tcs = new TaskCompletionSource<TOutput>();
        var key = Guid.NewGuid().ToString();
        _tasks.TryAdd(key, tcs);
        
        await messageProducer.ProduceAsync(key, value);
        logger.LogInformation("Message with key {Key} sent", key);
        
        await Task.WhenAny(tcs.Task, Task.Delay(_timeout));
        logger.LogInformation("Task with key {Key} completed", key);
        
        _tasks.TryRemove(key, out _);

        if (tcs.Task.IsCompleted)
            return tcs.Task.Result;

        throw new TimeoutException();
    }

    internal void Handle(string key, TOutput value)
    {
        if (_tasks.TryRemove(key, out var tcs))
        {
            logger.LogInformation("Message with key {Key} received", key);
            tcs.SetResult(value);
        }
    }

    public void Dispose() => messageProducer.Dispose();
}
