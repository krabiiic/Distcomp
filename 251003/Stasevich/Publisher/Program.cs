using Microsoft.EntityFrameworkCore;
using StackExchange.Redis;
using System.Text.Json;
using System.Text.Json.Serialization;
using WebApplication1.Data;
using WebApplication1.Entity;
using WebApplication1.Middleware;
using WebApplication1.Repository;
using WebApplication1.Service;

namespace WebApplication1
{
    public partial class Program
    {
        public static async Task Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);

            var connectionString = builder.Configuration.GetConnectionString("DefaultConnection");
            builder.Services.AddDbContext<AppDbContext>(options =>
                options.UseNpgsql(connectionString, npgsqlOptions =>
                {
                    npgsqlOptions.MigrationsHistoryTable("__EFMigrationsHistory", "distcomp");
                }));

            builder.Services.AddScoped<IRepository<User>, EfRepository<User>>();
            builder.Services.AddScoped<IRepository<Article>, EfRepository<Article>>();
            builder.Services.AddScoped<IRepository<Sticker>, EfRepository<Sticker>>();
            builder.Services.AddScoped<IRepository<Note>, EfRepository<Note>>();

            var redis = ConnectionMultiplexer.Connect("localhost:6379");
            builder.Services.AddSingleton<IConnectionMultiplexer>(redis);
            builder.Services.AddSingleton<IRedisCacheService, RedisCacheService>();

            builder.Services.AddScoped<IUserService, UserService>();
            builder.Services.AddScoped<IArticleService, ArticleService>();
            builder.Services.AddScoped<IStickerService, StickerService>();

            builder.Services.AddScoped<IRemoteNoteService, RemoteNoteService>();
            builder.Services.AddHttpClient<IRemoteNoteService, RemoteNoteService>(client =>
            {
                client.BaseAddress = new Uri("http://localhost:24130/api/v1.0/");
            });

            builder.Services.AddSingleton<KafkaNoteProducerService>();
            builder.Services.AddSingleton<KafkaNoteStatusUpdaterService>();

            builder.Services.AddControllers()
                .AddJsonOptions(options =>
                {
                    options.JsonSerializerOptions.PropertyNamingPolicy = JsonNamingPolicy.CamelCase;
                });


            builder.Services.AddLogging(logging =>
            {
                logging.ClearProviders();
                logging.AddConsole();
                logging.SetMinimumLevel(LogLevel.Information);
            });

            var app = builder.Build();

            app.UseMiddleware<ErrorHandlingMiddleware>();

            app.MapControllers();

            app.Urls.Add("http://localhost:24110");

            using (var scope = app.Services.CreateScope())
            {
                var userService = scope.ServiceProvider.GetRequiredService<IUserService>();
                try
                {
                    await userService.CreateUserAsync(new DTO.UserRequestTo
                    {
                        Login = "yana08469@gmail.com",
                        Password = "123456",
                        Firstname = "Янина",
                        Lastname = "Стасевич"
                    });
                }
                catch (ValidationException)
                {
                }
            }

            var kafkaConsumer = app.Services.GetRequiredService<KafkaNoteStatusUpdaterService>();
            var kafkaCts = new CancellationTokenSource();
            Task.Run(() => kafkaConsumer.RunAsync(kafkaCts.Token));

            await app.RunAsync();
        }
    }
}
