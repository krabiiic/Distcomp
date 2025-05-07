namespace Discussion.Storage;

public sealed class CassandraInitializer(
    CassandraDbContext context,
    ILogger<CassandraInitializer> logger) : BackgroundService
{
    public override async Task StartAsync(CancellationToken cancellationToken)
    {
        logger.LogInformation("CassandraInitializer is starting");
        await context.Notices.CreateIfNotExistsAsync();
        logger.LogInformation("CassandraInitializer is stopping");
        
        await base.StartAsync(cancellationToken);
    }

    protected override Task ExecuteAsync(CancellationToken stoppingToken) => Task.CompletedTask;

    public override void Dispose()
    {
        context.Dispose();
        base.Dispose();
    }
}
