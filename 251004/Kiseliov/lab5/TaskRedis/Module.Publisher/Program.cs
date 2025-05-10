using Asp.Versioning;
using Publisher.Clients.Discussion;
using Publisher.Clients.Discussion.Dto.Message;
using Publisher.Clients.Discussion.KafkaClient;
using Publisher.Constants;
using Publisher.ExceptionHandlers;
using Publisher.Extensions;
using Publisher.Services.Implementations;
using Publisher.Services.Interfaces;
using Publisher.Storage;
using TaskRedis.ServiceDefaults;
using TaskRedis.ServiceDefaults.Kafka;
using TaskRedis.ServiceDefaults.Kafka.Serialization;
var builder = WebApplication.CreateBuilder(args);
builder.AddServiceDefaults();

builder.Services.AddControllers()
    .AddJsonOptions(options => { options.JsonSerializerOptions.IncludeFields = true; });

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddApiVersioning(config =>
{
    config.DefaultApiVersion = new ApiVersion(1, 0);
    config.AssumeDefaultVersionWhenUnspecified = true;
});

builder.AddRedisOutputCache("redis");
builder.Services.AddOutputCache(options =>
{
    options.AddBasePolicy(policyBuilder => policyBuilder
        .With(context => context.HttpContext.Request.Path.StartsWithSegments("/api/v1.0/creators"))
        .Tag(CacheTags.Creators));
    options.AddBasePolicy(policyBuilder => policyBuilder
        .With(context => context.HttpContext.Request.Path.StartsWithSegments("/api/v1.0/notices"))
        .Tag(CacheTags.Notices));
    options.AddBasePolicy(policyBuilder => policyBuilder
        .With(context => context.HttpContext.Request.Path.StartsWithSegments("/api/v1.0/tags"))
        .Tag(CacheTags.Tags));
    options.AddBasePolicy(policyBuilder => policyBuilder
        .With(context => context.HttpContext.Request.Path.StartsWithSegments("/api/v1.0/tweets"))
        .Tag(CacheTags.Tweets));
});

builder.AddKafkaSyncClient<InTopicMessage, OutTopicMessage>("kafka", settings =>
{
    settings.InTopic = "in-echo";
    settings.OutTopic = "out-echo";
    settings.Timeout = TimeSpan.FromSeconds(10);
    settings.ValueSerializer = new KafkaJsonSerializer<InTopicMessage>();
    settings.ValueDeserializer = new KafkaJsonDeserializer<OutTopicMessage>();
});

builder.AddNpgsqlDbContext<AppDbContext>("distcomp-publisher");
builder.Services.AddMigration<AppDbContext>();

builder.Services.AddScoped<IDiscussionService, KafkaDiscussionServiceClient>();
builder.Services.AddScoped<ICreatorService, CreatorService>();
builder.Services.AddScoped<INoticeService, NoticeService>();
builder.Services.AddScoped<ITagService, TagService>();
builder.Services.AddScoped<ITweetService, TweetService>();

builder.Services.AddExceptionHandler<GlobalExceptionHandler>();
builder.Services.AddProblemDetails();

builder.Services.AddSwaggerGen();

var app = builder.Build();
app.UseExceptionHandler();
app.MapControllers();

app.UseOutputCache();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.MapDefaultEndpoints();

app.UseHttpsRedirection();
app.UseAuthorization();
app.Run();
