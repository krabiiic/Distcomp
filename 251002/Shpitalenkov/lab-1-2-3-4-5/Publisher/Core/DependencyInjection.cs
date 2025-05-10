using System.Reflection;
using System.Text.Json;
using System.Text.Json.Serialization;
using Core.Interfaces;
using Core.Kafka;
using Core.Repositories.InMemoryRepositories;
using Core.Repositories.PostgersRepositories;
using Core.Repositories.Redis;
using Core.Serialization;
using Core.Services;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace Core;

public static class DependencyInjection
{
    public static void AddApplication(this IServiceCollection services)
    {
        services.AddSingleton<ICreatorService, CreatorService>();
        services.AddSingleton<IArticleService, ArticleService>();
        services.AddSingleton<ITagService, TagService>();
        services.AddAutoMapper(Assembly.GetExecutingAssembly());
    }

    public static IServiceCollection AddRepositories(this IServiceCollection services){


        services.AddSingleton<ITagRepository, PgTagRepository>();
        services.AddSingleton<ICreatorRepository, PgCreatorRepository>();
        services.AddSingleton<IArticleRepository, PgArticleRepository>(); 

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
        services.AddSingleton<KafkaPublisher<long, MessageRequest>>(sp =>
        {
            var bootstrapServers = config["Kafka:BootstrapServer"];
            var topic = config["Kafka:PostEventsTopic"];

            return new KafkaPublisher<long, MessageRequest>(
                bootstrapServers,
                topic,
                keySerializer: null, 
                valueSerializer: new SystemTextJsonSerializer<MessageRequest>(
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