using Cassandra;
using CassandraPosts.Repositories;
using CassandraPosts.Services;
using Confluent.Kafka;
using System.Text.Json;
using CassandraPosts.Data;

var builder = WebApplication.CreateBuilder(args);

// Регистрация Cassandra
var cluster = Cluster.Builder()
    .AddContactPoint("localhost")
    .Build();

// Применяем миграции (создаем keyspace и таблицы)
CassandraMigrations.ApplyMigrations(cluster);

// Теперь регистрируем сервисы с подключением к созданному keyspace
builder.Services.AddSingleton<ICluster>(cluster);
builder.Services.AddSingleton<IPostRepository, PostRepository>();
builder.Services.AddSingleton<PostService>();
builder.Services.AddSingleton<IdMappingService>();

// Регистрация Kafka Producer
builder.Services.AddSingleton<IProducer<Null, string>>(sp =>
{
    var config = new ProducerConfig { BootstrapServers = "localhost:9092" };
    return new ProducerBuilder<Null, string>(config).Build();
});

// Регистрация Kafka Consumer Service
builder.Services.AddSingleton<IHostedService, KafkaConsumerService>();

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

app.UseSwagger();
app.UseSwaggerUI();
app.UseHttpsRedirection();
app.MapControllers();
app.Run();