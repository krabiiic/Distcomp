using Lab3.Application.Extensions;


var builder = WebApplication.CreateBuilder(args);
// Регистрация контроллеров
builder.Services.AddControllers();
// Настройка сервисов: репозитории, Kafka, валидаторы
builder.Services.ConfigureServices();

var app = builder.Build();

// Настройка middleware
app.ConfigureMiddleware();
app.UseHttpsRedirection();
app.UseAuthorization();
app.MapControllers();

app.Run();
