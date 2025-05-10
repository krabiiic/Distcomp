using Lab3.Infrastructure.MappingProfiles;
using Lab3.Core.Abstractions;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.EntityFrameworkCore;
using Lab3.Infrastructure.Repositories;
using Lab3.Infrastructure.Confs;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Options;
using MongoDB.Driver;

namespace Lab3.Infrastructure.Extensions
{
    public static class InfrastructureExtensions
    {
        // Метод для настройки подключения к MongoDB
        static private void ConfigureDatabase(this IServiceCollection services)
        {
            // Регистрация настроек MongoDB из переменных окружения
            services.Configure<MongoDbOptions>(
                options =>
                {
                    options.ConnectionString = Environment.GetEnvironmentVariable("CONNECTION_STRING")!;
                    options.DatabaseName = Environment.GetEnvironmentVariable("DB_NAME")!;
                });
            // Регистрация MongoDB-клиента как синглтона
            services.AddSingleton<IMongoClient>(serviceProvider =>
            {
                // Получение настроек из DI
                var settings = serviceProvider.GetRequiredService<IOptions<MongoDbOptions>>().Value;
                return new MongoClient(settings.ConnectionString);
            });
            services.AddScoped(serviceProvider =>
             {
                 var settings = serviceProvider.GetRequiredService<IOptions<MongoDbOptions>>().Value;
                 var client = serviceProvider.GetRequiredService<IMongoClient>();
                 return client.GetDatabase(settings.DatabaseName);
             });
        }
        static private void ConfigureMapping(this IServiceCollection services)
        {
            services.AddAutoMapper(typeof(InfrastructureMappingProfile));
        }

        static public void ConfigureRepositories(this IServiceCollection services)
        {
            // Настройка MongoDB
            services.ConfigureDatabase();
            // Настройка маппинга
            services.ConfigureMapping();
            // Регистрация репозитория для работы с постами
            services.AddScoped<IPostRepository, PostRepository>();
        }
    }
}
