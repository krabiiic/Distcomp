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
            services.ConfigureRepositories();
            services.AddAutoMapper(typeof(ApplicationMappingProfile));
            services.AddScoped<IValidator<Message>, MessageValidator>();
            services.AddScoped<IMessageService, MessageService>();

            services.AddSingleton<IProducer, KafkaProducer>();
            services.AddHostedService<KafkaConsumer>();
        }
        public static void ConfigureMiddleware(this IApplicationBuilder app)
        {
            app.UseMiddleware<ExceptionHandlingMiddleware>();
        }
    }
}
