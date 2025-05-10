using FluentMigrator.Runner;
using FluentMigrator.Runner.Processors;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Options;

namespace Infrastructure;

public class Postgres
{
    public static void MapCompositeTypes(){
        Dapper.DefaultTypeMap.MatchNamesWithUnderscores = true;
    }

    public static void AddMigrations(IServiceCollection services){
        services.AddFluentMigratorCore()
            .AddSingleton(s => s.GetRequiredService<IOptionsSnapshot<ProcessorOptions>>().Value)
            .ConfigureRunner(rb => rb.AddPostgres()
                .WithGlobalConnectionString(s => {
                    var config = s.GetRequiredService<IOptions<InfrastructureSettings>>();
                    return config.Value.PostgresConnectionString;
                })  
                .ScanIn(typeof(Postgres).Assembly).For.Migrations())
            .AddLogging(lb => lb.AddFluentMigratorConsole());
    }
}