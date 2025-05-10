using DistComp_1.Extensions;
using DistComp_1.Infrastructure.Mapper;
using DistComp_1.Infrastructure.Validators;
using DistComp_1.Middleware;
using FluentValidation;
using Scalar.AspNetCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();

builder.Services.AddOpenApi();
builder.Services.AddEndpointsApiExplorer();


builder.Services.AddAutoMapper(typeof(MappingProfile));
builder.Services.AddValidatorsFromAssemblyContaining<AuthorRequestDTOValidator>();

builder.Services.AddRepositories();
builder.Services.AddServices();

var app = builder.Build();
app.UseMiddleware<GlobalExceptionMiddleware>();
if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
    app.MapScalarApiReference(options =>
    {
        options.WithTheme(ScalarTheme.DeepSpace);
    });
}

app.UseAuthorization();

app.MapControllers();

app.Run();