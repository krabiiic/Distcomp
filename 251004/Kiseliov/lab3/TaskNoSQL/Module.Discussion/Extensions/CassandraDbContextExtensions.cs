using Discussion.Storage;
namespace Discussion.Extensions;

public static class CassandraDbContextExtensions
{
    public static void AddCassandraDbContext(
        this IHostApplicationBuilder builder,
        string connectionName)
    {
        var connectionString = builder.Configuration.GetConnectionString(connectionName);

        if (string.IsNullOrEmpty(connectionString))
            throw new ArgumentException("Connection string is not found");

        builder.Services.AddSingleton(new CassandraDbOptions(connectionString));
        builder.Services.AddSingleton<CassandraDbContext>();
    }
}
