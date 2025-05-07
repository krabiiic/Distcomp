using Microsoft.Extensions.DependencyInjection;
using Lab1.Infrastructure.Extensions;
using Lab1.Core.Abstractions;
using Lab1.Application.Services;
using Lab1.Application.MappingProfiles;
using FluentValidation;
using Lab1.Application.Validators;
using Lab1.Core.Models;
using Microsoft.AspNetCore.Builder;
using Lab1.Application.Middleware;
using StackExchange.Redis;

namespace Lab1.Application.Extensions
{
    public static class ApplicationExtensions
    {
        public static void ConfigureServices(this IServiceCollection services)
        {
            InfrastructureExtensions.ConfigureRepositories(services, Environment.GetEnvironmentVariable("CONNECTION_STRING"));

            services.AddAutoMapper(typeof(ApplicationMappingProfile));
            services.AddScoped<IValidator<Creator>, CreatorValidator>();
            services.AddScoped<IValidator<Issue>, IssueValidator>();
            services.AddScoped<IValidator<Sticker>, StickerValidator>();

            services.AddScoped<ICreatorService, CreatorService>();
            services.AddScoped<IIssueService, IssueService>();
            services.AddScoped<IStickerService, StickerService>();
            services.AddSingleton<IConnectionMultiplexer>(ConnectionMultiplexer.Connect(Environment.GetEnvironmentVariable("REDIS")));
            // Добавляем Hosted Service для создания топиков
            services.AddHostedService<KafkaTopicInitializer>();

            // Теперь регистрируем Kafka Producer и Consumer
            services.AddScoped<IProducer, KafkaProducer>();
            services.AddSingleton<IConsumer, KafkaConsumer>();
            services.AddHostedService<KafkaConsumerService>();
        }

        public static void ConfigureMiddleware(this IApplicationBuilder app)
        {
            app.UseMiddleware<ExceptionHandlingMiddleware>();
        }
    }
}
