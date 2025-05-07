using Aspire.Confluent.Kafka;
using Confluent.Kafka;
using Confluent.Kafka.SyncOverAsync;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using TaskKafka.ServiceDefaults.Kafka.Consumer;
using TaskKafka.ServiceDefaults.Kafka.Producer;
using TaskKafka.ServiceDefaults.Kafka.Serialization;
using TaskKafka.ServiceDefaults.Kafka.SyncClient;
namespace TaskKafka.ServiceDefaults.Kafka;

public static class RegisterServiceExtensions
{
    private const string DefaultConfigSectionName = "Aspire:Confluent:Kafka:Consumer";

    public static void AddKafkaMessageHandler<TK, TV, THandler>(
        this IHostApplicationBuilder builder,
        string connectionName,
        Action<KafkaHandlerSettings<TK, TV>> configureHandlerSettings,
        Action<KafkaConsumerSettings>? configureConsumerSettings = null)
        where THandler : class, IKafkaHandler<TK, TV>
        where TK : class
        where TV : class
    {
        var settings = new KafkaHandlerSettings<TK, TV>();
        configureHandlerSettings(settings);
        
        builder.AddKafkaConsumer<TK, TV>(
            connectionName,
            configureConsumerSettings,
            consumerBuilder =>
            {
                consumerBuilder.SetKeyDeserializer(settings.KeyDeserializer.AsSyncOverAsync());
                consumerBuilder.SetValueDeserializer(settings.ValueDeserializer.AsSyncOverAsync());
            });

        builder.Services.AddSingleton<IKafkaHandler<TK, TV>, THandler>();
        builder.Services.AddHostedService<KafkaBackgroundService<TK, TV>>();
        builder.Services.Configure<KafkaHandlerSettings<TK, TV>>(handlerSettings =>
        {
            KafkaConsumerSettings consumerSettings = BuildConsumerSettings(builder, DefaultConfigSectionName, configureConsumerSettings, connectionName);
            configureConsumerSettings?.Invoke(consumerSettings);
            
            configureHandlerSettings(handlerSettings);
            handlerSettings.ConsumerConfig = consumerSettings.Config;
            handlerSettings.ConsumerConfig.EnablePartitionEof = true;
            handlerSettings.ConsumerConfig.AutoOffsetReset = AutoOffsetReset.Earliest;
        });
    }

    public static void AddKafkaMessageProducer<TK, TV>(
        this IHostApplicationBuilder builder,
        string connectionName,
        Action<KafkaMessageProducerSettings<TK, TV>> configureMessageProducerSettings,
        Action<KafkaProducerSettings>? configureProducerSettings = null)
        where TK : class
        where TV : class
    {
        var settings = new KafkaMessageProducerSettings<TK, TV>();
        configureMessageProducerSettings(settings);
        
        builder.AddKafkaProducer<TK, TV>(
            connectionName,
            configureProducerSettings,
            producerBuilder =>
            {
                producerBuilder.SetKeySerializer(settings.KeySerializer.AsSyncOverAsync());
                producerBuilder.SetValueSerializer(settings.ValueSerializer.AsSyncOverAsync());
            });

        builder.Services.AddSingleton<KafkaMessageProducer<TK, TV>>();
        builder.Services.Configure(configureMessageProducerSettings);
    }

    public static void AddKafkaSyncClient<TInput, TOutput>(
        this IHostApplicationBuilder builder,
        string connectionName,
        Action<KafkaSyncClientSetting<TInput, TOutput>> configureTopicSettings)
        where TInput : class
        where TOutput : class
    {
        KafkaSyncClientSetting<TInput, TOutput> topicSettings = new KafkaSyncClientSetting<TInput, TOutput>();
        configureTopicSettings(topicSettings);

        builder.AddKafkaMessageProducer<string, TInput>(
            connectionName,
            settings =>
            {
                settings.Topic = topicSettings.InTopic;
                settings.KeySerializer = new KafkaStringSerializer();
                settings.ValueSerializer = topicSettings.ValueSerializer;
            });

        builder.Services.AddSingleton<KafkaSyncClient<TInput, TOutput>>();

        builder.AddKafkaMessageHandler<string, TOutput, SyncClientHandler<TInput, TOutput>>(
            connectionName,
            settings =>
            {
                settings.Topic = topicSettings.OutTopic;
                settings.KeyDeserializer = new KafkaStringDeserializer();
                settings.ValueDeserializer = topicSettings.ValueDeserializer;
            },
            settings => settings.Config.GroupId = $"{topicSettings.OutTopic}-sync-client");
    }

    private static KafkaConsumerSettings BuildConsumerSettings(IHostApplicationBuilder builder, string configurationSectionName, Action<KafkaConsumerSettings>? configureSettings, string connectionName)
    {
        var configSection = builder.Configuration.GetSection(configurationSectionName);
        KafkaConsumerSettings settings = new();
        configSection.Bind(settings);

        configSection.GetSection(nameof(KafkaConsumerSettings.Config)).Bind(settings.Config);

        if (builder.Configuration.GetConnectionString(connectionName) is {} connectionString)
        {
            settings.ConnectionString = connectionString;
        }

        configureSettings?.Invoke(settings);
        
        if (settings.ConnectionString is not null)
            settings.Config.BootstrapServers = settings.ConnectionString;

        if (settings.Metrics)
            settings.Config.StatisticsIntervalMs ??= 1000;
        return settings;
    }
}