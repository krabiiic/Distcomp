using DistComp_1.Repositories.Implementations;
using DistComp_1.Repositories.Interfaces;
using DistComp_1.Services.Implementations;
using DistComp_1.Services.Interfaces;

namespace DistComp_1.Extensions;

public static class ServiceExtensions
{
    public static IServiceCollection AddRepositories(this IServiceCollection services)
    {
        services.AddSingleton<IAuthorRepository, InMemoryAuthorRepository>();
        services.AddSingleton<INewsRepository, InMemoryNewsRepository>();
        services.AddSingleton<ILabelRepository, InMemoryLabelRepository>();
        services.AddSingleton<IReactionRepository, InMemoryReactionRepository>();

        return services;
    }

    public static IServiceCollection AddServices(this IServiceCollection services)
    {
        services.AddScoped<IAuthorService, AuthorService>();
        services.AddScoped<INewsService, NewsService>();
        services.AddScoped<ILabelService, LabelService>();
        services.AddScoped<IReactionService, ReactionService>();
        
        return services;
    }
}