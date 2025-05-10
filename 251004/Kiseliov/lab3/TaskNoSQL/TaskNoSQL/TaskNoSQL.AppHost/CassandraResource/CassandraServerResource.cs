namespace TaskNoSQL.AppHost.CassandraResource;

public class CassandraServerResource : ContainerResource, IResourceWithConnectionString
{
    internal const string PrimaryEndpointName = "tcp";

    public CassandraServerResource(string name) : base(name)
    {
        PrimaryEndpoint = new EndpointReference(this, PrimaryEndpointName);
    }

    public EndpointReference PrimaryEndpoint { get; }

    private ReferenceExpression ConnectionString =>
        ReferenceExpression.Create(
            $"Host={PrimaryEndpoint.Property(EndpointProperty.Host)};Port={PrimaryEndpoint.Property(EndpointProperty.Port)}");

    public ReferenceExpression ConnectionStringExpression
    {
        get
        {
            if (this.TryGetLastAnnotation<ConnectionStringRedirectAnnotation>(out var connectionStringAnnotation))
            {
                return connectionStringAnnotation.Resource.ConnectionStringExpression;
            }

            return ConnectionString;
        }
    }

    public ValueTask<string?> GetConnectionStringAsync(CancellationToken cancellationToken = default)
    {
        if (this.TryGetLastAnnotation<ConnectionStringRedirectAnnotation>(out var connectionStringAnnotation))
        {
            return connectionStringAnnotation.Resource.GetConnectionStringAsync(cancellationToken);
        }

        return ConnectionStringExpression.GetValueAsync(cancellationToken);
    }
    
    private readonly Dictionary<string, string> _keyspaces = new(StringComparer.OrdinalIgnoreCase);

    /// <summary>
    /// A dictionary where the key is the resource name and the value is the database name.
    /// </summary>
    public IReadOnlyDictionary<string, string> Keyspaces => _keyspaces;

    internal void AddKeyspace(string name, string keyspaceName)
    {
        _keyspaces.TryAdd(name, keyspaceName);
    }
}