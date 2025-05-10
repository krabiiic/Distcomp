using Discussion.Data;
using Discussion.Infrastructure.Mapper;
using Discussion.Infrastructure.Validators;
using Discussion.Repositories.Implementations;
using Discussion.Repositories.Interfaces;
using Discussion.Services.Implementations;
using Discussion.Services.Interfaces;
using FluentValidation;

namespace Discussion.Extensions;

public static class ServiceProviderExtensions
{
    public static IServiceCollection AddCassandra(this IServiceCollection services, IConfiguration config)
    {
        var address = config.GetValue<string>("CASSANDRA_HOST");
        var schema = config.GetValue<string>("SCHEMA");
        var session = new CassandraConnector(address!, schema!).GetSession();
        services.AddSingleton(session);
            
        return services;
    }
    
    public static IServiceCollection AddRepositories(this IServiceCollection services)
    {
        services.AddScoped<IMessageRepository, CassandraMessageRepository>();

        return services;
    }
    
    public static IServiceCollection AddServices(this IServiceCollection services)
    {
        services.AddScoped<IMessageService, MessageService>();

        return services;
    }
    
    public static IServiceCollection AddInfrastructure(this IServiceCollection services)
    {
        services.AddAutoMapper(typeof(MappingProfile));
        services.AddValidatorsFromAssemblyContaining<MessageRequestDtoValidator>();

        return services;
    }
}