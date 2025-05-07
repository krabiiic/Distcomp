using Cassandra;
using ISession = Cassandra.ISession;
namespace Discussion.Storage;

public sealed class CassandraConnectionFactory(CassandraDbOptions options)
{
    private readonly Cluster _cluster = Cluster.Builder()
        .AddContactPoint(options.Host)
        .WithPort(options.Port)
        .WithSocketOptions(new SocketOptions().SetReadTimeoutMillis(90000))
        .WithReconnectionPolicy(new ConstantReconnectionPolicy(10000))
        .Build();
    
    public ISession Connect()
    {
        var retryCount = 3;
        while (true)
        {
            try
            {
                return _cluster.Connect(options.Keyspace);
            }
            catch (NoHostAvailableException)
            {
                if (retryCount == 0)
                    throw;
                
                retryCount--;
            }
        }
    }
}
