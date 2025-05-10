using System.Text.Json;
using System.Text.Json.Serialization;
using Domain.Repositories;
using Infrastructure.Kafka;
using Infrastructure.Repositories.MongoDb;
using Infrastructure.Serialization;
using Infrastructure.Settings;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace Infrastructure;

public static class DependencyInjection
{
    public static IServiceCollection AddRepositories(this IServiceCollection services){


        services.AddSingleton<IPostRepository, PostMongoDbRepository>();

        return services;
    }
    
    public static IServiceCollection AddInfrastructure (
        this IServiceCollection services,
        IConfigurationRoot config
    ){
        
        services.Configure<MongoDbOptions>(config.GetSection("PostDatabase"));
        return services;
    }
    
    public static IServiceCollection AddKafkaPublishers(
        this IServiceCollection services,
        IConfigurationRoot config
    )
    {
        services.AddSingleton<KafkaPublisher<long, PostResponse>>(sp =>
        {
            var bootstrapServers = config["Kafka:BootstrapServer"];
            var topic = config["Kafka:PostResponsesTopic"];

            return new KafkaPublisher<long, PostResponse>(
                bootstrapServers,
                topic,
                keySerializer: null, 
                valueSerializer: new SystemTextJsonSerializer<PostResponse>(
                    new JsonSerializerOptions { Converters = { new JsonStringEnumConverter() } }
                )
            );
        });

        return services;
    }
}