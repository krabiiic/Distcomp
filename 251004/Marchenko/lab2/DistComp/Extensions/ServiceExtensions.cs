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
        services.AddScoped<IMarkRepository, DatabaseMarkRepository>();
        services.AddScoped<IMessageRepository, DatabaseMessageRepository>();

        return services;
    }

    public static IServiceCollection AddServices(this IServiceCollection services)
    {
        services.AddScoped<ICreatorService, CreatorService>();
        services.AddScoped<IIssueService, IssueService>();
        services.AddScoped<IMarkService, MarkService>();
        services.AddScoped<IMessageService, MessageService>();
        
        return services;
    }
    
    public static IServiceCollection AddDbContext(this IServiceCollection services, IConfiguration config)
    {
        services.AddDbContext<AppDbContext>(options =>
            options.UseNpgsql(config.GetConnectionString("PostgresConnection")));

        return services;
    }
    
    public static void ApplyMigrations(this IApplicationBuilder app, IServiceProvider services)
    {
        using var scope = services.CreateScope();
        var db = scope.ServiceProvider.GetRequiredService<AppDbContext>();

        var migrations = db.Database.GetPendingMigrations();

        if (migrations.Any())
        {
            db.Database.Migrate();
        }
    }
}