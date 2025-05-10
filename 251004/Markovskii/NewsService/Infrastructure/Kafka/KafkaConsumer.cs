using System.Threading.Channels;
using Confluent.Kafka;
using Microsoft.Extensions.Logging;
using System.Linq;

namespace Infrastructure.Kafka;

public class KafkaConsumer<TKey, TValue> : IDisposable
{
    private const int ChannelCapacity = 10; 
    private readonly TimeSpan _bufferDelay = TimeSpan.FromMilliseconds(30); 

    private readonly Channel<ConsumeResult<TKey, TValue>> _channel;
    private readonly IConsumer<TKey, TValue> _consumer;
    private readonly IHandler<TKey, TValue> _handler;

    private readonly ILogger<KafkaConsumer<TKey, TValue>>? _logger;

    public KafkaConsumer(
        string bootstrapServers,
        string topic,
        string groupId,
        IHandler<TKey, TValue> handler,
        IDeserializer<TKey>? keyDeserializer,
        IDeserializer<TValue>? valueDeserializer,
        ILogger<KafkaConsumer<TKey, TValue>>? logger = null
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

        var buffer = new List<ConsumeResult<TKey, TValue>>(ChannelCapacity);
        var timer = new PeriodicTimer(_bufferDelay);

        await foreach (var message in _channel.Reader.ReadAllAsync(token))
        {
            buffer.Add(message);
            if (buffer.Count >= ChannelCapacity || await timer.WaitForNextTickAsync(token))
            {
                await ProcessBatch(buffer, token);
                buffer.Clear();
            }
        }
        
        if (buffer.Count > 0)
        {
            await ProcessBatch(buffer, token);
        }
    }

    private async Task ProcessBatch(List<ConsumeResult<TKey, TValue>> batch, CancellationToken token)
    {
        bool processed = false;
        while (!processed && !token.IsCancellationRequested)
        {
            try
            {
                await _handler.Handle(batch, token);
            
                var partitionOffsets = batch
                    .GroupBy(r => r.Partition.Value)
                    .Select(g => g.OrderByDescending(r => r.Offset.Value).First());
            
                foreach (var offset in partitionOffsets)
                {
                    _consumer.StoreOffset(offset);
                }
            
                processed = true;
            }
            catch (Exception ex) when (ex is not OperationCanceledException)
            {
                _logger?.LogError(ex, "Error processing batch, retrying...");
                await Task.Delay(1000, token);
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
                        throw;
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
    public void Dispose() => _consumer.Close();
}