using Lab3.Infrastructure.MappingProfiles;
using Lab3.Core.Abstractions;
using Microsoft.Extensions.DependencyInjection;
using Lab3.Infrastructure.Repositories;
using Lab3.Infrastructure.Confs;
using Microsoft.Extensions.Options;
using MongoDB.Driver;

namespace Lab3.Infrastructure.Extensions
{
    public static class InfrastructureExtensions
    {
        static private void ConfigureDatabase(this IServiceCollection services)
        {
            services.Configure<MongoDbOptions>(
                options =>
                {
                    options.ConnectionString = Environment.GetEnvironmentVariable("CONNECTION_STRING")!;
                    options.DatabaseName = Environment.GetEnvironmentVariable("DB_NAME")!;
                });
            services.AddSingleton<IMongoClient>(serviceProvider =>
            {
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
            services.ConfigureDatabase();
            services.ConfigureMapping();
            services.AddScoped<IMessageRepository, MessageRepository>();
        }
    }
}
