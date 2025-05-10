using System.Text.Json;
using System.Text.Json.Serialization;
using Domain.Repository;
using Infrastructure.Kafka;
using Infrastructure.Repositories.InMemoryRepositories;
using Infrastructure.Repositories.PostgersRepositories;
using Infrastructure.Repositories.Redis;
using Infrastructure.Serialization;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace Infrastructure;

public static class DependencyInjection
{
    public static IServiceCollection AddRepositories(this IServiceCollection services){


        services.AddSingleton<IMarkRepository, PgMarkRepository>();
        services.AddSingleton<IEditorRepository, PgEditorRepository>();
        services.AddSingleton<INewsRepository, PgNewsRepository>(); 

        return services;
    }

    public static IServiceCollection AddInfrastructure (
        this IServiceCollection services,
        IConfigurationRoot config
    ){
        
        services.Configure<InfrastructureSettings>(options => {
            options.PostgresConnectionString = config.GetConnectionString("npg");
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
        services.AddSingleton<KafkaPublisher<long, PostRequest>>(sp =>
        {
            var bootstrapServers = config["Kafka:BootstrapServer"];
            var topic = config["Kafka:PostEventsTopic"];

            return new KafkaPublisher<long, PostRequest>(
                bootstrapServers,
                topic,
                keySerializer: null, 
                valueSerializer: new SystemTextJsonSerializer<PostRequest>(
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
        services.Configure<RedisSettings>(options =>
            options.ConnectionString = config.GetConnectionString("redis")
        );

        services.AddSingleton<IRedisCacheService, RedisCacheService>();
        return services;
    }
}