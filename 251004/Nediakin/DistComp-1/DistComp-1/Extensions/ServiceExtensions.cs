﻿using DistComp_1.Repositories.Implementations;
using DistComp_1.Repositories.Interfaces;
using DistComp_1.Services.Implementations;
using DistComp_1.Services.Interfaces;

namespace DistComp_1.Extensions;

public static class ServiceExtensions
{
    public static IServiceCollection AddRepositories(this IServiceCollection services)
    {
        services.AddSingleton<ICreatorRepository, InMemoryCreatorRepository>();
        services.AddSingleton<IIssueRepository, InMemoryIssueRepository>();
        services.AddSingleton<IStickerRepository, InMemoryStickerRepository>();
        services.AddSingleton<INoteRepository, InMemoryNoteRepository>();

        return services;
    }

    public static IServiceCollection AddServices(this IServiceCollection services)
    {
        services.AddScoped<ICreatorService, CreatorService>();
        services.AddScoped<IIssueService, IssueService>();
        services.AddScoped<IStickerService, StickerService>();
        services.AddScoped<INoteService, NoteService>();
        
        return services;
    }
}