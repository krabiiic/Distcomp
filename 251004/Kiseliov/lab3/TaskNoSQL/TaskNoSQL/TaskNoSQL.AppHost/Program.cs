using Projects;
using TaskNoSQL.AppHost.CassandraResource;
var builder = DistributedApplication.CreateBuilder(args);

var postgres = builder.AddPostgres("postgres-publisher", port: 5432)
    .AddDatabase("distcomp-publisher", "distcomp");

var cassandra = builder.AddCassandra("cassandra-discussion", port: 9042)
    .WithDataVolume()
    .AddKeyspace("distcomp-discussion", "distcomp");

var discussion = builder.AddProject<Module_Discussion>("discussion")
    .WithHttpEndpoint(24130)
    .WithReference(cassandra);

var publisher = builder.AddProject<Module_Publisher>("publisher")
    .WithHttpEndpoint(24110)
    .WithReference(postgres)
    .WithReference(discussion);

var app = builder.Build();
app.Run();
