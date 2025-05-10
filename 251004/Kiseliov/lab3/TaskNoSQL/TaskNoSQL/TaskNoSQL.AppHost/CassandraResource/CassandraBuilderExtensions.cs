namespace TaskNoSQL.AppHost.CassandraResource;

public static class CassandraBuilderExtensions
{
    public static IResourceBuilder<CassandraServerResource> AddCassandra(this IDistributedApplicationBuilder builder,
        string name,
        string? clusterName = null,
        int? port = null)
    {
        var postgresServer = new CassandraServerResource(name);
        return builder.AddResource(postgresServer)
            .WithEndpoint(port: port, targetPort: 9042, name: CassandraServerResource.PrimaryEndpointName)
            .WithImage("cassandra")
            .WithEnvironment("CASSANDRA_CLUSTER_NAME", clusterName ?? $"cassandra-cluster-{name}")
            .WithEnvironment("CASSANDRA_ENDPOINT_SNITCH", "GossipingPropertyFileSnitch")
            .WithEnvironment("CASSANDRA_DC", "dc1")
            .WithEnvironment("CASSANDRA_RACK", "rack1");
    }
    
    public static IResourceBuilder<CassandraKeyspaceResource> AddKeyspace(this IResourceBuilder<CassandraServerResource> builder, string name, string? keyspaceName = null)
    {
        keyspaceName ??= name;

        builder.Resource.AddKeyspace(name, keyspaceName);
        var postgresDatabase = new CassandraKeyspaceResource(name, keyspaceName, builder.Resource);
        return builder.ApplicationBuilder.AddResource(postgresDatabase);
    }
    
    public static IResourceBuilder<CassandraServerResource> WithDataVolume(this IResourceBuilder<CassandraServerResource> builder, string? name = null, bool isReadOnly = false)
        => builder.WithVolume(name ?? CreateVolumeName(builder, "data"), "/var/lib/cassandra", isReadOnly);
    
    private static string CreateVolumeName(IResourceBuilder<CassandraServerResource> builder, string suffix)
    {
        var applicationName = builder.ApplicationBuilder.Environment.ApplicationName;
        var resourceName = builder.Resource.Name;
        return $"{(HasOnlyValidChars(applicationName) ? applicationName : "volume")}-{resourceName}-{suffix}";
    }
    
    private static bool HasOnlyValidChars(string name)
    {
        var nameSpan = name.AsSpan();

        for (var i = 0; i < nameSpan.Length; i++)
        {
            var c = nameSpan[i];

            if (i == 0 && !(char.IsAsciiLetter(c) || char.IsNumber(c)))
                return false;
            
            if (!(char.IsAsciiLetter(c) || char.IsNumber(c) || c == '_' || c == '.' || c == '-'))
                return false;
        }

        return true;
    }
}