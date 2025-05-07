using LAB4.Data;
using LAB4.Models;
using LAB4.Repositories;
using LAB4.Services;
using Microsoft.EntityFrameworkCore;
using Confluent.Kafka;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Регистрация контекста БД
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseNpgsql(builder.Configuration.GetConnectionString("Default")));

// Регистрация репозиториев
builder.Services.AddScoped(typeof(IRepository<>), typeof(EfRepository<>));

// Регистрация сервисов
builder.Services.AddScoped<IssueService>();
builder.Services.AddScoped<StickerService>();
builder.Services.AddScoped<WriterService>();
builder.Services.AddScoped<PostService>();

// Регистрация Kafka Producer
builder.Services.AddSingleton<IProducer<Null, string>>(sp =>
{
    var config = new ProducerConfig
    {
        BootstrapServers = "localhost:9092"
    };
    return new ProducerBuilder<Null, string>(config).Build();
});

var app = builder.Build();

// Автоматическое применение миграций
using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<AppDbContext>();
    db.Database.Migrate();
}

app.MapControllers();
app.UseSwagger();
app.UseSwaggerUI();
app.Run();