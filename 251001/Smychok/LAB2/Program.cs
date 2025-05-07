using LAB2.Data;
using LAB2.Models;
using LAB2.Repositories;
using LAB2.Services;
using Microsoft.EntityFrameworkCore;

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

record WeatherForecast(DateOnly Date, int TemperatureC, string? Summary)
{
    public int TemperatureF => 32 + (int)(TemperatureC / 0.5556);
}
