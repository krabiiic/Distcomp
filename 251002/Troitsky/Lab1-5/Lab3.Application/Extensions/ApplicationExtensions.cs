using Microsoft.Extensions.DependencyInjection;
using Lab3.Infrastructure.Extensions;
using Lab3.Core.Abstractions;
using Lab3.Application.Services;
using Lab3.Application.MappingProfiles;
using FluentValidation;
using Lab3.Application.Validators;
using Lab3.Core.Models;
using Microsoft.AspNetCore.Builder;
using Lab3.Application.Middleware;

namespace Lab3.Application.Extensions
{
    public static class ApplicationExtensions
    {
        public static void ConfigureServices(this IServiceCollection services)
        {
            // Регистрирует репозитории , настройки MongoDB/Redis и маппинги
            services.ConfigureRepositories();
            // Регистрирует профиль, определяющий правила преобразования между DTO и моделями
            services.AddAutoMapper(typeof(ApplicationMappingProfile));
            // Регистрирует валидатор PostValidator
            services.AddScoped<IValidator<Post>, PostValidator>();
            // Реализация PostService содержит бизнес-логику CRUD постов
            services.AddScoped<IPostService, PostService>();

            services.AddSingleton<IProducer, KafkaProducer>();
            // KafkaConsumer работает как фоновая служба (IHostedService)
            services.AddHostedService<KafkaConsumer>();
        }
        public static void ConfigureMiddleware(this IApplicationBuilder app)
        {
            // добавление middleware в конвейер обработки запросов
            app.UseMiddleware<ExceptionHandlingMiddleware>();
        }
    }
}
