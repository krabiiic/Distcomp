using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Storage;

namespace publisher.Extensions
{
    public static class MigrationExtensions
    {
        public static IServiceCollection AddMigration<TContext>(this IServiceCollection services) where TContext : DbContext =>
            services.AddHostedService<MigrationHostedService<TContext>>();

        private class MigrationHostedService<TContext>(IServiceProvider serviceProvider) : BackgroundService
            where TContext : DbContext
        {
            public override Task StartAsync(CancellationToken cancellationToken) =>
                MigrateDbContext(serviceProvider);

            protected override Task ExecuteAsync(CancellationToken stoppingToken) =>
                Task.CompletedTask;

            private static async Task MigrateDbContext(IServiceProvider serviceProvider)
            {
                using IServiceScope scope = serviceProvider.CreateScope();
                IServiceProvider scopedProvider = scope.ServiceProvider;

                var context = scopedProvider.GetRequiredService<TContext>();
                var logger = scopedProvider.GetRequiredService<ILogger<TContext>>();

                try
                {
                    logger.LogInformation("Migrating database associated with context {DbContextName}", typeof(TContext).Name);

                    IExecutionStrategy strategy = context.Database.CreateExecutionStrategy();

                    await strategy.ExecuteAsync(async () => await context.Database.MigrateAsync());
                }
                catch (Exception e)
                {
                    logger.LogError(e, "An error occurred while migrating the database used on context {DbContextName}", typeof(TContext).Name);

                    throw;
                }
            }
        }
    }
}
