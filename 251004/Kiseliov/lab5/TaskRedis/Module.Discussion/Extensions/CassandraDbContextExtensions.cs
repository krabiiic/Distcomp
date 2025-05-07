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

        ParseConnectionString(connectionString, out var host, out var port, out var keyspace);
        builder.Services.AddSingleton(new CassandraDbOptions(host, port, keyspace));
        builder.Services.AddSingleton<CassandraConnectionFactory>();
        builder.Services.AddScoped<CassandraDbContext>();
        builder.Services.AddHostedService<CassandraInitializer>();
    }

    private static void ParseConnectionString(string connectionString, out string host, out int port, out string keyspace)
    {
        var args = connectionString.Split(';');
        host = args[0].Split('=')[1];
        port = int.Parse(args[1].Split('=')[1]);
        keyspace = args[2].Split('=')[1];
    }
}
