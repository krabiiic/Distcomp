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
        services.AddScoped<ICreatorRepository, DatabaseCreatorRepository>();
        services.AddScoped<IIssueRepository, DatabaseIssueRepository>();
        services.AddScoped<IStickerRepository, DatabaseStickerRepository>();
        services.AddScoped<INoteRepository, DatabaseNoteRepository>();

        return services;
    }

    public static IServiceCollection AddServices(this IServiceCollection services)
    {
        services.AddScoped<ICreatorService, CreatorService>();
        services.AddScoped<IIssueService, IssueService>();
        services.AddScoped<IStickerService, StickerService>();
        services.AddScoped<INoteService, NoteService>();
        
        return services;
    }
    
    public static IServiceCollection AddDbContext(this IServiceCollection services, IConfigurationManager config)
    {
        services.AddDbContext<AppDbContext>(options =>
            options.UseNpgsql(config.GetConnectionString("PostgresConnection")));

        return services;
    }
}