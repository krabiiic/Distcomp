using System.Text.Json.Serialization;
using Publisher.Extensions;
using Publisher.Infrastructure.Mapper;
using Publisher.Infrastructure.Validators;
using Publisher.Middleware;
using FluentValidation;
using Microsoft.EntityFrameworkCore;
using Publisher.Data;
using Scalar.AspNetCore;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services
    .AddControllers()
    .AddJsonOptions(options =>
    {
        options.JsonSerializerOptions.ReferenceHandler = ReferenceHandler.IgnoreCycles;
    });

builder.Services.AddOpenApi();
builder.Services.AddEndpointsApiExplorer();

// Infrastructure
builder.Services.AddAutoMapper(typeof(MappingProfile));
// Регистрируем все валидаторы из сборки (текущей), где находится StoryRequestDTOValidator
// Scoped lifetime
builder.Services.AddValidatorsFromAssemblyContaining<UserRequestDTOValidator>();

builder.Services.AddRepositories();
builder.Services.AddServices();
builder.Services.AddDiscussionClient();
builder.Services.AddDbContext(builder.Configuration);

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

app.ApplyMigrations(app.Services);

app.Run();

public static class AppExtensions
{
    public static void ApplyMigrations(this IApplicationBuilder app, IServiceProvider services)
    {
        using var scope = services.CreateScope();
        var db = scope.ServiceProvider.GetRequiredService<AppDbContext>();

        var migrations = db.Database.GetPendingMigrations();

        if (migrations.Any())
        {
            db.Database.Migrate();
        }
    }
}