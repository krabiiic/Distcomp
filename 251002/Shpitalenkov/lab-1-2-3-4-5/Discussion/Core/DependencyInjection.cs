using System.Reflection;
using System.Text.Json;
using System.Text.Json.Serialization;
using Core.Interfaces;
using Core.Kafka;
using Core.Repositories.MongoDb;
using Core.Serialization;
using Core.Services;
using Core.Settings;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace Core;

public static class DependencyInjection
{
    public static void AddApplication(this IServiceCollection services)
    {
        services.AddSingleton<IMessageService, MessageService>();
        services.AddAutoMapper(Assembly.GetExecutingAssembly());
    }
    public static IServiceCollection AddRepositories(this IServiceCollection services){


        services.AddSingleton<IMessageRepository, MessageMongoDbRepository>();

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
        services.AddSingleton<KafkaPublisher<long, MessageResponse>>(sp =>
        {
            var bootstrapServers = config["Kafka:BootstrapServer"];
            var topic = config["Kafka:PostResponsesTopic"];

            return new KafkaPublisher<long, MessageResponse>(
                bootstrapServers,
                topic,
                keySerializer: null, 
                valueSerializer: new SystemTextJsonSerializer<MessageResponse>(
                    new JsonSerializerOptions { Converters = { new JsonStringEnumConverter() } }
                )
            );
        });

        return services;
    }
}