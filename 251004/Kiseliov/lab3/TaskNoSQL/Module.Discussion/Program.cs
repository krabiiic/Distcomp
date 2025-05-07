using Asp.Versioning;
using Discussion.ExceptionHandlers;
using Discussion.Extensions;
using Discussion.Services.Implementations;
using Discussion.Services.Interfaces;
using Discussion.Storage;
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

builder.AddCassandraDbContext("distcomp-discussion");
builder.Services.AddScoped<CassandraDbContext>();
builder.Services.AddScoped<INoticeService, NoticeService>();
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
