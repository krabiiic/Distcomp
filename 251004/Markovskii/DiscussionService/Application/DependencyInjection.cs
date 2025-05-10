using System.Reflection;
using Application.Interfaces;
using Application.Services;
using Microsoft.Extensions.DependencyInjection;

namespace Application;

public static class DependencyInjection
{
    public static void AddApplication(this IServiceCollection services)
    {
        services.AddSingleton<IPostService, PostService>();
        services.AddAutoMapper(Assembly.GetExecutingAssembly());
    }
}