using Confluent.Kafka;
using Infrastructure.Settings;

public static class ServiceCollectionExtensions
{
    public static IServiceCollection AddKafkaConsumers(
        this IServiceCollection services,
        IConfigurationRoot config
    )
    {
        services.Configure<KafkaOptions>(config.GetSection("Kafka"));

        services.AddSingleton<ReactionHandler>();
        services.AddHostedService<KafkaReactionBackgroundService>();
        return services;
    }
}
