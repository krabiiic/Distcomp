using Lab1.Application.Extensions;
using Lab1.Web.Controllers;

// Объект builder - настраиваются сервисы и конфигурация приложения
var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
// Регистрация HttpClient, внедряемого в конструктор PostController
builder.Services.AddHttpClient<PostController>(); 
// Регистрация в контейнере всех сервисов, репозиториев, валидаторов и прочих внедряемых компонентов
builder.Services.ConfigureServices();

var app = builder.Build();
// Добавление ExceptionHandlingMiddleware в конвейер
app.ConfigureMiddleware();

app.UseAuthorization();

// Сканирует все контроллеры в проекте и настраивает маршруты по атрибутам [Route], [HttpGet] и тд
app.MapControllers();

app.Run();
