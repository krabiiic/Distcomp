using Lab3.Application.Extensions;


var builder = WebApplication.CreateBuilder(args);
// ����������� ������������
builder.Services.AddControllers();
// ��������� ��������: �����������, Kafka, ����������
builder.Services.ConfigureServices();

var app = builder.Build();

// ��������� middleware
app.ConfigureMiddleware();
app.UseHttpsRedirection();
app.UseAuthorization();
app.MapControllers();

app.Run();
