using System.Threading.Channels;
using Confluent.Kafka;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using Shared.Infrastructure.Kafka;

public sealed class KafkaAsyncConsumer<TKey, TValue> : IDisposable
{
    private const int ChannelCapacity = 10; // TODO: IOptions
    private readonly TimeSpan _bufferDelay = TimeSpan.FromMilliseconds(30); // TODO: IOptions

    private readonly Channel<ConsumeResult<TKey, TValue>> _channel;
    private readonly IConsumer<TKey, TValue> _consumer;
    private readonly IHandler<TKey, TValue> _handler;

    private readonly ILogger<KafkaAsyncConsumer<TKey, TValue>>? _logger;

    public KafkaAsyncConsumer(
        string bootstrapServers,
        string topic,
        string groupId,
        IHandler<TKey, TValue> handler,
        IDeserializer<TKey>? keyDeserializer,
        IDeserializer<TValue>? valueDeserializer,
        ILogger<KafkaAsyncConsumer<TKey, TValue>>? logger = null
    )
    {
        _logger = logger;
        _handler = handler;

        _channel = Channel.CreateBounded<ConsumeResult<TKey, TValue>>(
            new BoundedChannelOptions(ChannelCapacity)
            {
                SingleWriter = true,
                SingleReader = true,
                AllowSynchronousContinuations = true,
                FullMode = BoundedChannelFullMode.Wait,
            }
        );

        var builder = new ConsumerBuilder<TKey, TValue>(
            new ConsumerConfig
            {
                BootstrapServers = bootstrapServers,
                GroupId = groupId,
                AutoOffsetReset = AutoOffsetReset.Earliest,
                EnableAutoCommit = true,
                EnableAutoOffsetStore = false,
                /*MetadataMaxAgeMs = 5000,*/
            }
        );

        if (keyDeserializer is not null)
        {
            builder.SetKeyDeserializer(keyDeserializer);
        }

        if (valueDeserializer is not null)
        {
            builder.SetValueDeserializer(valueDeserializer);
        }

        _consumer = builder.Build();
        _consumer.Subscribe(topic);
    }

    public Task Consume(CancellationToken token)
    {
        var handle = HandleCore(token);
        var consume = ConsumeCore(token);

        return Task.WhenAll(handle, consume);
    }

    private async Task HandleCore(CancellationToken token)
    {
        await Task.Yield();

        await foreach (
            var consumeResults in _channel
                .Reader.ReadAllAsync(token)
                .Buffer(ChannelCapacity, _bufferDelay)
                .WithCancellation(token)
        )
        {
            token.ThrowIfCancellationRequested();

            while (true)
            {
                try
                {
                    await _handler.Handle(consumeResults, token);
                }
                catch (Exception ex)
                {
                    _logger.LogError(ex, "Unhandled exception occurred");
                    continue;
                }

                var partitionLastOffsets = consumeResults.GroupBy(
                    r => r.Partition.Value,
                    (_, f) => f.MaxBy(p => p.Offset.Value)
                );

                foreach (var partitionLastOffset in partitionLastOffsets)
                {
                    _consumer.StoreOffset(partitionLastOffset);
                }

                break; // TODO: Polly.Retry
            }
        }
    }

    private async Task ConsumeCore(CancellationToken token)
    {
        await Task.Yield();
        try
        {
            _logger?.LogInformation("CONSUME CORE CALLED");
            while (!token.IsCancellationRequested)
            {
                try
                {
                    var result = _consumer.Consume(token);
                    if (result != null)
                    {
                        _logger?.LogInformation("Message received");
                        await _channel.Writer.WriteAsync(result, token);
                    }
                }
                catch (ConsumeException ex)
                {
                    _logger?.LogError(ex, "Consume error: {Reason}", ex.Error.Reason);
                    if (ex.Error.IsFatal)
                    {
                        throw; // Terminate on fatal errors
                    }
                }
            }
        }
        catch (OperationCanceledException)
        {
            _logger.LogInformation("Consume operation cancelled");
        }
        finally
        {
            _channel.Writer.Complete();
        }
    }

    /*private async Task ConsumeCore(CancellationToken token)*/
    /*{*/
    /**/
    /**/
    /*    await Task.Yield();*/
    /**/
    /*    while (_consumer.Consume(token) is { } result)*/
    /*    {*/
    /*        _logger?.LogInformation("consumecore called");*/
    /*        await _channel.Writer.WriteAsync(result, token);*/
    /*_logger.LogTrace(*/
    /*    "{Partition}:{Offset}:WriteToChannel",*/
    /*    result.Partition.Value,*/
    /*    result.Offset.Value*/
    /*);*/
    /*}*/
    /**/
    /*    _channel.Writer.Complete();*/
    /*}*/

    public void Dispose() => _consumer.Close();
}
