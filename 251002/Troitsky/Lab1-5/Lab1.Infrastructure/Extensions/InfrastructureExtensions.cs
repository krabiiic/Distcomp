using Lab1.Infrastructure.MappingProfiles;
using Lab1.Core.Abstractions;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.EntityFrameworkCore;
using Lab1.Infrastructure.Repositories;
using Lab1.Infrastructure.Contexts;
namespace Lab1.Infrastructure.Extensions
{
    public static class InfrastructureExtensions
    {
        static private void ConfigureDatabase(this IServiceCollection services, string dbConnectionString)
        {
            services.AddSingleton<AppDatabase>();
            services.AddDbContext<DataContext>
               (
               options => options.UseNpgsql(dbConnectionString)
               );
        }
        // Преобразует сущности базы данных в DTO и обратно
        static private void ConfigureMapping(this IServiceCollection services)
        {
            services.AddAutoMapper(typeof(InfrastructureMappingProfile));
        }
        static public void ConfigureRepositories(this IServiceCollection services, string dbConnectionString)
        {
            services.ConfigureDatabase(dbConnectionString);
            services.ConfigureMapping();

            //Регистрирует репозитории
            services.AddScoped<IUserRepository, UserRepository>();
            services.AddScoped<INewsRepository, NewsRepository>();
            services.AddScoped<ITagRepository, TagRepository>();
//            services.AddScoped<IPostRepository, PostRepository>();
        }
    }
}
