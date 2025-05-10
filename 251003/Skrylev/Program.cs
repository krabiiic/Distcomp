using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Hosting;
using System;
using System.Text.Json;
using Microsoft.EntityFrameworkCore;
using MyApp.Repositories;
using MongoDB.Driver;
using MyApp.Services;
using StackExchange.Redis;

namespace MyApp
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);

            builder.Services.AddDbContext<AppDbContext>(options =>
                options.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection")));

            builder.Services.AddScoped(typeof(IGenericRepository<>), typeof(PostgresRepository<>));

            builder.Services.AddScoped<IEditorService, EditorService>();
            builder.Services.AddScoped<ILabelService, LabelService>();
            builder.Services.AddScoped<IStoryService, StoryService>();
            builder.Services.AddScoped<INoteService, NoteService>();

            var mongoUrl = builder.Configuration.GetConnectionString("MongoDb");
            var mongoClient = new MongoClient(mongoUrl);
            var database = mongoClient.GetDatabase("distcomp");

            builder.Services.AddSingleton<IMongoClient>(mongoClient);
            builder.Services.AddScoped<IMongoDatabase>(sp => database);

            builder.Services.AddHttpClient<INoteClient, NoteClient>(client =>
            {
                client.BaseAddress = new Uri("http://localhost:24130/api/v1.0/notes/");
            });

            builder.Services.AddControllers()
                .AddJsonOptions(options =>
                {
                    options.JsonSerializerOptions.PropertyNamingPolicy = JsonNamingPolicy.CamelCase;
                });

            builder.Services.AddScoped<IDiscussionNoteRepository, DiscussionNoteRepository>();
            builder.Services.AddScoped<IDiscussionNoteService, DiscussionNoteService>();

            builder.WebHost.UseUrls("http://localhost:24110", "http://localhost:24130");

            builder.Services.AddSingleton<KafkaNoteProducer>();
            builder.Services.AddSingleton<KafkaNoteStatusConsumer>();
            builder.Services.AddSingleton<KafkaNoteConsumerDiscussion>();
            builder.Services.AddSingleton<KafkaNoteStatusProducerDiscussion>();

            builder.Services.AddHostedService<DiscussionNoteModerationBackgroundService>();
            builder.Services.AddSingleton<RedisCacheService>();
            var redisConnectionString = builder.Configuration.GetSection("Redis")["ConnectionString"];

            builder.Services.AddSingleton<IConnectionMultiplexer>(sp =>
                ConnectionMultiplexer.Connect(redisConnectionString));

            var app = builder.Build();

            app.UseRouting();
            
            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });

            using (var scope = app.Services.CreateScope())
            {
                var services = scope.ServiceProvider;
                var context = services.GetRequiredService<AppDbContext>();
                context.Database.Migrate();
            }

            app.Run();
        }
    }
}