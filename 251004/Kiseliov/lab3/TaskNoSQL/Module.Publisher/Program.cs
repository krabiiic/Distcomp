using Asp.Versioning;
using Publisher.Clients.Discussion;
using Publisher.ExceptionHandlers;
using Publisher.Extensions;
using Publisher.Services.Implementations;
using Publisher.Services.Interfaces;
using Publisher.Storage;
using Refit;
using TaskNoSQL.ServiceDefaults;
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

builder.AddNpgsqlDbContext<AppDbContext>("distcomp-publisher");
builder.Services.AddMigration<AppDbContext>();
builder.Services.AddRefitClient<IDiscussionService>()
    .ConfigureHttpClient(static client => client.BaseAddress = new Uri("http://discussion/api/v1.0"));
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

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.MapDefaultEndpoints();

app.UseHttpsRedirection();
app.UseAuthorization();
app.Run();
