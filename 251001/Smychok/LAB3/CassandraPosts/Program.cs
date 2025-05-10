using Cassandra;
using CassandraPosts.Repositories;
using CassandraPosts.Services;

var builder = WebApplication.CreateBuilder(args);

// Регистрация Cassandra
builder.Services.AddSingleton<ICluster>(_ => 
    Cluster.Builder()
        .AddContactPoint("localhost")
        .Build());

// Регистрация репозитория
builder.Services.AddScoped<IPostRepository, PostRepository>();

// Регистрация сервиса
builder.Services.AddScoped<PostService>();

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

app.UseSwagger();
app.UseSwaggerUI();
app.UseHttpsRedirection();
app.MapControllers();
app.Run();