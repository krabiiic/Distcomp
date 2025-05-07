using Core;
using Infrastructure;
using WebApi.Extensions;
using WebApi.Settings;

var builder = WebApplication.CreateBuilder(args);
builder.Services.Configure<DiscussionSettings>(
    builder.Configuration.GetSection("DiscussionSettings")
);
builder.Services.AddHttpClient();


builder.Services.AddRepositories();
builder.Services.AddKafkaPublishers(builder.Configuration);
builder.Services.AddKafkaConsumers(builder.Configuration);
builder.Services.AddRedis(builder.Configuration);

builder.Services.AddStackExchangeRedisCache(options =>
{
    options.Configuration = builder.Configuration.GetConnectionString("redis");
});

builder.Services.AddInfrastructure(builder.Configuration);
builder.Services.AddApplication();

builder.Services.AddControllers();

builder.Services.AddSwaggerGen();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
var app = builder.Build();

app.UseExceptionHandler("/errors");
app.UseSwagger();
app.UseSwaggerUI();
app.UseHttpsRedirection();
app.MigrateUp();
app.MapControllers();

app.Run();