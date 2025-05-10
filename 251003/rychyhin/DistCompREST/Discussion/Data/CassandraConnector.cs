using Cassandra;
using ISession = Cassandra.ISession;

namespace Discussion.Data;

public class CassandraConnector : IDisposable
{
    private readonly Cluster _cluster;
    private readonly ISession _session;

    public CassandraConnector(string contactPoint, string keyspace)
    {
        // Разделяем host и port
        var parts = contactPoint.Split(':');
        var host = parts[0];
        var port = parts.Length > 1 ? int.Parse(parts[1]) : 9042;
    
        _cluster = Cluster.Builder()
            .AddContactPoint(host)
            .WithPort(port)
            .Build();
        _session = _cluster.Connect(keyspace);
    }

    public ISession GetSession() => _session;

    public void Dispose()
    {
        _session?.Dispose();
        _cluster?.Dispose();
    }
}