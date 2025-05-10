using publisher.Extensions;
using publisher.ToDiscussion;
using publisher.ExceptionHandler;

using publisher.Services.Implementations;
using publisher.Services.Interfaces;
using publisher.Storage;
using Refit;
using System;
using Microsoft.EntityFrameworkCore;
//using TaskNoSQL.ServiceDefaults;


var builder = WebApplication.CreateBuilder(args);
//builder.AddServiceDefaults();

builder.Services.AddControllers()
    .AddJsonOptions(options => { options.JsonSerializerOptions.IncludeFields = true; });

builder.Services.AddEndpointsApiExplorer();


//builder.AddNpgsqlDbContext<AppDbContext>("distcomp-publisher");
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection")));

builder.Services.AddMigration<AppDbContext>();
/*
builder.Services.AddRefitClient<IDiscussionService>()
    .ConfigureHttpClient(static client => client.BaseAddress = new Uri("http://discussion/api/v1.0"));
*/
builder.Services.AddRefitClient<IDiscussionService>()
    .ConfigureHttpClient(client =>
    {
        client.BaseAddress = new Uri("http://localhost:24130/api/v1.0/");
        client.Timeout = TimeSpan.FromSeconds(5); // чтобы тесты не падали из-за задержки
    });

builder.Services.AddScoped<ICreatorService, CreatorService>();
builder.Services.AddScoped<INoteService, NoteService>();
builder.Services.AddScoped<ITagService, TagService>();
builder.Services.AddScoped<IArticleService, ArticleService>();
builder.Services.AddExceptionHandler<GlobalExceptionHandler>();
builder.Services.AddProblemDetails();
builder.Services.AddSwaggerGen();


/*
builder.Services.AddHttpClient("discussion", client =>
{
    client.BaseAddress = new Uri("http://localhost:24130/");
});
*/

var app = builder.Build();
app.UseExceptionHandler();
app.MapControllers();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}



//app.MapDefaultEndpoints();

app.UseHttpsRedirection();
app.UseAuthorization();
app.Run();