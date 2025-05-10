using Infrastructure.Settings;
using Shared.Infrastructure.Kafka;

public static class ServiceCollectionExtensions
{
    public static IServiceCollection AddKafkaConsumers(
        this IServiceCollection services,
        IConfigurationRoot config
    )
    {
        services.Configure<KafkaOptions>(config.GetSection("Kafka"));

        services.AddSingleton<KafkaResponseDispatcher<ReactionResponse>>();
        services.AddSingleton<ReactionResponseHandler>();
        services.AddHostedService<KafkaResponseBackgroundService>();
        return services;
    }
}
