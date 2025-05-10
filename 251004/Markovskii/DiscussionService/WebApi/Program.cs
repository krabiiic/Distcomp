using Application;
using Infrastructure;
using WebApi.Extensions;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddRepositories();
builder.Services.AddKafkaPublishers(builder.Configuration);
builder.Services.AddKafkaConsumers(builder.Configuration);
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
app.MapControllers();

app.Run();