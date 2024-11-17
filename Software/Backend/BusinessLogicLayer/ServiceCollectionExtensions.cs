﻿using BusinessLogicLayer.AppLogic.Users;
using BusinessLogicLayer.Infrastructure;
using DataAccessLayer;
using DataAccessLayer.AppLogic;
using DataAccessLayer.Infrastructure;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace BusinessLogicLayer;

/// <summary>
/// Klasa za registriranje dodatnih servisa potrebnih za DataAccessLayer, a koji se ne mogu direktno ubaciti u WebApi projekt jer nema referencu na isti
/// </summary>
public static class ServiceCollectionExtensions
{
    public static IServiceCollection AddDataAccessLayerServices(
        this IServiceCollection services,
        IConfiguration configuration
    )
    {
        services.Configure<DBOptions>(
            configuration.GetSection("Database")
        );

        services.AddScoped<DB>(); // Zbog DI u repozitorije

        // Repozitoriji
        services.AddScoped<IUserRepository, UserRepository>();

        // Servisi
        services.AddScoped<IUserService, UserService>();

        return services;
    }
}