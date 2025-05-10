using Lab1.Application.Extensions;
using Lab1.Web.Controllers;

// ������ builder - ������������� ������� � ������������ ����������
var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
// ����������� HttpClient, ����������� � ����������� PostController
builder.Services.AddHttpClient<PostController>(); 
// ����������� � ���������� ���� ��������, ������������, ����������� � ������ ���������� �����������
builder.Services.ConfigureServices();

var app = builder.Build();
// ���������� ExceptionHandlingMiddleware � ��������
app.ConfigureMiddleware();

app.UseAuthorization();

// ��������� ��� ����������� � ������� � ����������� �������� �� ��������� [Route], [HttpGet] � ��
app.MapControllers();

app.Run();
