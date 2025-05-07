using DistComp.Data;
using DistComp.Repositories.Implementations;
using DistComp.Repositories.Interfaces;
using DistComp.Services.Implementations;
using DistComp.Services.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace DistComp.Extensions;

public static class ServiceExtensions
{
    public static IServiceCollection AddRepositories(this IServiceCollection services)
    {
        services.AddScoped<IAuthorRepository, DatabaseAuthorRepository>();
        services.AddScoped<INewsRepository, DatabaseNewsRepository>();
        services.AddScoped<ILabelRepository, DatabaseLabelRepository>();
        services.AddScoped<IReactionRepository, DatabaseReactionRepository>();

        return services;
    }

    public static IServiceCollection AddServices(this IServiceCollection services)
    {
        services.AddScoped<IAuthorService, AuthorService>();
        services.AddScoped<INewsService, NewsService>();
        services.AddScoped<ILabelService, LabelService>();
        services.AddScoped<IReactionsService, ReactionsService>();

        return services;
    }

    public static IServiceCollection AddDbContext(this IServiceCollection services, IConfigurationManager config)
    {
        services.AddDbContext<AppDbContext>(options =>
            options.UseNpgsql(config.GetConnectionString("PostgresConnection")));

        return services;
    }
}