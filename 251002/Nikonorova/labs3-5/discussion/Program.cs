using discussion.ExceptionHandler;
using discussion.Extensions;
using discussion.Services.Implementations;
using discussion.Services.Interfaces;
using discussion.Storage;
using Microsoft.Extensions.Hosting; // Для IHostApplicationBuilder
//using tasknosql.ServiceDefaults; // Раскомментируйте эту строку
//using TaskNoSQL.ServiceDefaults;


var builder = WebApplication.CreateBuilder(args);
//builder.AddServiceDefaults();

builder.Services.AddControllers()
    .AddJsonOptions(options => { options.JsonSerializerOptions.IncludeFields = true; });

builder.Services.AddEndpointsApiExplorer();


builder.AddCassandraDbContext("discussion");
builder.Services.AddScoped<CDBContext>();
builder.Services.AddScoped<INoteService, NoteService>();
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

//app.MapDefaultEndpoints();

app.UseHttpsRedirection();
app.UseAuthorization();
app.Run();