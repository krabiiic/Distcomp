using Cassandra;
using ISession = Cassandra.ISession;

namespace Discussion.Data;

public class CassandraConnector : IDisposable
{
    private readonly Cluster _cluster;
    private readonly ISession _session;

    public CassandraConnector(string contactPoint, string keyspace)
    {
        _cluster = Cluster.Builder()
            .AddContactPoint(contactPoint)
            .WithPort(9042)
            .Build();
        var session = _cluster.Connect();
        session.ChangeKeyspace("distcomp");
        _session = session;

    }

    public ISession GetSession() => _session;

    public void Dispose()
    {
        _session?.Dispose();
        _cluster?.Dispose();
    }
}