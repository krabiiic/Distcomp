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
        // Регистрирует в контейнере все сервисы, репозитории, валидаторы и прочие внедряемые компоненты
        public static void ConfigureServices(this IServiceCollection services)
        {
            // регистрирует DbContext/клиент Mongo, репозитории
            InfrastructureExtensions.ConfigureRepositories(services, Environment.GetEnvironmentVariable("CONNECTION_STRING"));

            // регистрирует профиль AutoMapper MappingProfiles
            services.AddAutoMapper(typeof(ApplicationMappingProfile));
            // регистрирует на каждую доменную модель свой валидатор
            services.AddScoped<IValidator<User>, UserValidator>();
            services.AddScoped<IValidator<News>, NewsValidator>();
            services.AddScoped<IValidator<Tag>, TagValidator>();
//            services.AddScoped<IValidator<Post>, PostValidator>();


            // конкретные реализации сервисов
            services.AddScoped<IUserService, UserService>();
            services.AddScoped<INewsService, NewsService>();
            services.AddScoped<ITagService, TagService>();
            services.AddSingleton<IConnectionMultiplexer>(ConnectionMultiplexer.Connect(Environment.GetEnvironmentVariable("REDIS")));
            //            services.AddScoped<IPostService, PostService>();

            // для создания топиков Kafka
            services.AddHostedService<KafkaTopicInitializer>();

            // регистрация у Kafka Producer и Consumer
            services.AddScoped<IProducer, KafkaProducer>();
            services.AddSingleton<IConsumer, KafkaConsumer>();
            services.AddHostedService<KafkaConsumerService>();
        }

        public static void ConfigureMiddleware(this IApplicationBuilder app)
        {
            //Добавляет в конвейер ExceptionHandlingMiddleware
            //Все входящие запросы сначала попадут в middleware, затем пойдут дальше к контроллерам
            // на обратном пути — ошибки будут ловиться и обрабатываться централизованно
            app.UseMiddleware<ExceptionHandlingMiddleware>();
        }
    }
}
