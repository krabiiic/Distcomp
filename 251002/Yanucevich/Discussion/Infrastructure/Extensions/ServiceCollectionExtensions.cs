using System;
using System.Text.Json;
using System.Text.Json.Serialization;
using Domain.Repositories;
using Infrastructure.Repositories.Mongo;
using Infrastructure.Serialization;
using Infrastructure.Settings;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Shared.Domain;
using Shared.Infrastructure.Kafka;

namespace Infrastructure.Extensions;

public static class ServiceCollectionExtensions
{
    public static IServiceCollection AddRepositories(this IServiceCollection services)
    {
        // services.AddSingleton<IMarkerRepository, MarkerRepositoryInMemory>();
        // services.AddSingleton<IUserRepository, UserRepositoryInMemory>();
        // services.AddSingleton<ITweetRepository, TweetRepositoryInMemory>();
        // services.AddSingleton<IReactionRepository, ReactionRepositoryInMemory>();

        services.AddSingleton<IReactionRepository, ReactionRepositoryMongo>();

        return services;
    }

    public static IServiceCollection AddMongoInfrastructure(
        this IServiceCollection services,
        IConfigurationRoot config
    )
    {
        services.Configure<MongoOptions>(config.GetSection("ReactionDatabase"));
        return services;
    }

    public static IServiceCollection AddKafkaBackgroundService(this IServiceCollection services)
    {
        /*services.AddHostedService<>();*/
        return services;
    }

    public static IServiceCollection AddKafkaPublishers(
        this IServiceCollection services,
        IConfigurationRoot config
    )
    {
        services.AddSingleton<KafkaPublisher<long, ReactionResponse>>(sp =>
        {
            var bootstrapServers = config["Kafka:BootstrapServer"];
            var topic = config["Kafka:ReactionResponsesTopic"];

            return new KafkaPublisher<long, ReactionResponse>(
                bootstrapServers,
                topic,
                keySerializer: null, // Use default serializer for string keys
                valueSerializer: new SystemTextJsonSerializer<ReactionResponse>(
                    new JsonSerializerOptions { Converters = { new JsonStringEnumConverter() } }
                )
            );
        });

        return services;
    }
}
