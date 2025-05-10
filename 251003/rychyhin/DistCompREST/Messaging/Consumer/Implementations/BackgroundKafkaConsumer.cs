using Confluent.Kafka;
using Messaging.Consumer.Interfaces;
using Messaging.KafkaSerialization;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Options;

namespace Messaging.Consumer.Implementations;

public class BackgroundKafkaConsumer<TK, TV> : BackgroundService
{
    private readonly KafkaConsumerConfig _config;
    private readonly IServiceScopeFactory _serviceScopeFactory;

    public BackgroundKafkaConsumer(IOptions<KafkaConsumerConfig> config,
        IServiceScopeFactory serviceScopeFactory)
    {
        _serviceScopeFactory = serviceScopeFactory;
        _config = config.Value;
    }
    
    protected override Task ExecuteAsync(CancellationToken stoppingToken)
    {
        return Task.Run(() =>
        {
            using var consumer = new ConsumerBuilder<TK, TV>(_config)
                .SetValueDeserializer(new Deserializer<TV>())
                .Build();
            consumer.Subscribe(_config.Topic);

            try
            {
                while (!stoppingToken.IsCancellationRequested)
                {
                    var result = consumer.Consume(stoppingToken);
                    if (result != null)
                    {
                        using var scope = _serviceScopeFactory.CreateScope();
                        var handler = scope.ServiceProvider.GetRequiredService<IKafkaHandler<TK, TV>>();

                        handler.HandleAsync(result.Message.Key, result.Message.Value)
                            .GetAwaiter().GetResult();

                        consumer.Commit(result);
                        consumer.StoreOffset(result);
                    }
                }
            }
            catch (OperationCanceledException)
            {
                // wait till operation ended
            }
            finally
            {
                consumer.Close();
            }
        }, stoppingToken);
    }

}