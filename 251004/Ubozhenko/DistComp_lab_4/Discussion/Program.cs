using System.Text.Json.Serialization;
using Discussion.Consumers;
using Discussion.DTO.Request;
using Discussion.DTO.Response;
using Discussion.Extensions;
using Discussion.Middleware;
using Messaging;
using Messaging.Extensions;
using Scalar.AspNetCore;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers()
    .AddJsonOptions(options =>
    {
        options.JsonSerializerOptions.ReferenceHandler = ReferenceHandler.IgnoreCycles;
    });

// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
builder.Services.AddOpenApi();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddCassandra(builder.Configuration)
    .AddServices()
    .AddRepositories()
    .AddInfrastructure()
    .AddKafkaMessageBus()
    .AddKafkaProducer<string, KafkaMessage<ReactionResponseDTO>>(options =>
    {
        options.Topic = "OutTopic";
        options.BootstrapServers = Environment.GetEnvironmentVariable("KAFKA_BROKER");
        options.AllowAutoCreateTopics = true;
    })
    .AddKafkaConsumer<string, KafkaMessage<ReactionRequestDTO>, InTopicHandler>(options =>
    {
        options.Topic = "InTopic";
        options.AutoOffsetReset = Confluent.Kafka.AutoOffsetReset.Earliest;
        options.EnableAutoOffsetStore = false;
        options.GroupId = "reactions-group";
        options.BootstrapServers = Environment.GetEnvironmentVariable("KAFKA_BROKER");
        options.AllowAutoCreateTopics = true;  ///!!!!!!!!!!!!!!
    });

var app = builder.Build();

// Middleware для глобальных ошибок
app.UseMiddleware<GlobalExceptionMiddleware>();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
    app.MapScalarApiReference(options =>
    {
        options.WithTheme(ScalarTheme.DeepSpace);
    });
}

app.UseAuthorization();

app.MapControllers();

app.Run();