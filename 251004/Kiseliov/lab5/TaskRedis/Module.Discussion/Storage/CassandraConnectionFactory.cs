using Cassandra;
using ISession = Cassandra.ISession;
namespace Discussion.Storage;

public sealed class CassandraConnectionFactory(CassandraDbOptions options, ILogger<CassandraConnectionFactory> logger)
{
    private readonly Cluster _cluster = Cluster.Builder()
        .AddContactPoint(options.Host)
        .WithPort(options.Port)
        .WithDefaultKeyspace(options.Keyspace)
        .WithSocketOptions(new SocketOptions().SetReadTimeoutMillis(90000).SetConnectTimeoutMillis(90000))
        .WithReconnectionPolicy(new ConstantReconnectionPolicy(10000))
        .Build();
    
    public ISession Connect()
    {
        var retryCount = 5;
        while (true)
        {
            try
            {
                logger.LogInformation("Connecting to Cassandra cluster");
                return _cluster.ConnectAndCreateDefaultKeyspaceIfNotExists();
            }
            catch (NoHostAvailableException)
            {
                if (retryCount == 0)
                    throw;
                
                Thread.Sleep(5000);
                retryCount--;
                logger.LogWarning("Failed to connect to Cassandra cluster. Retrying...");
            }
        }
    }
}
