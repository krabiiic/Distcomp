using System;
using System.Text.Json;
using System.Text.Json.Serialization;
using Domain.Repositories;
using Infrastructure.Repositories.Postgres;
using Infrastructure.Serialization;
using Infrastructure.Settings;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
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

        services.AddSingleton<IMarkerRepository, MarkerRepositoryPg>();
        services.AddSingleton<IUserRepository, UserRepositoryPg>();
        services.AddSingleton<ITweetRepository, TweetRepositoryPg>();

        return services;
    }

    public static IServiceCollection AddPostgresInfrastructure(
        this IServiceCollection services,
        IConfigurationRoot config
    )
    {
        services.Configure<PostgresOptions>(options =>
        {
            options.PostgresConnectionString = config.GetConnectionString("npg");
            /*options.*/
        });

        Postgres.MapCompositeTypes();
        Postgres.AddMigrations(services);

        return services;
    }

    public static IServiceCollection AddKafkaPublishers(
        this IServiceCollection services,
        IConfigurationRoot config
    )
    {
        services.AddSingleton<KafkaPublisher<long, ReactionRequest>>(sp =>
        {
            var bootstrapServers = config["Kafka:BootstrapServer"];
            var topic = config["Kafka:ReactionEventsTopic"];

            return new KafkaPublisher<long, ReactionRequest>(
                bootstrapServers,
                topic,
                keySerializer: null, // Use default serializer for string keys
                valueSerializer: new SystemTextJsonSerializer<ReactionRequest>(
                    new JsonSerializerOptions { Converters = { new JsonStringEnumConverter() } }
                )
            );
        });

        return services;
    }

    public static IServiceCollection AddRedis(
        this IServiceCollection services,
        IConfigurationRoot config
    )
    {
        services.Configure<RedisOptions>(options =>
            options.ConnectionString = config.GetConnectionString("redis")
        );

        services.AddSingleton<IRedisCacheService, RedisCacheService>();
        return services;
    }
}
