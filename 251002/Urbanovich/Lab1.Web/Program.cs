using Lab1.Application.Extensions;
using Lab1.Web.Controllers;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddHttpClient<MessageController>();
builder.Services.ConfigureServices();

var app = builder.Build();
app.ConfigureMiddleware();

//app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
