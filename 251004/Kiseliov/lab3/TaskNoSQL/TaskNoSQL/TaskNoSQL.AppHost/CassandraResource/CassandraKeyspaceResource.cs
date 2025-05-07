using TaskNoSQL.AppHost.CassandraResource;
namespace TaskNoSQL.AppHost;

public class CassandraKeyspaceResource(string name, string keyspaceName, CassandraServerResource cassandraParentResource) : Resource(name), IResourceWithParent<CassandraServerResource>, IResourceWithConnectionString
{
    public CassandraServerResource Parent { get; } = cassandraParentResource;
    
    public ReferenceExpression ConnectionStringExpression =>
        ReferenceExpression.Create($"{Parent};Keyspace={KeyspaceName}");
    
    public string KeyspaceName { get; } = keyspaceName;
}
