using BusinessLogicLayer.AppLogic;
using BusinessLogicLayer.AppLogic.Geocoding;
using BusinessLogicLayer.AppLogic.JobRoom;
using BusinessLogicLayer.AppLogic.Jobs;
using BusinessLogicLayer.AppLogic.Reviews;
using BusinessLogicLayer.AppLogic.Skills;
using BusinessLogicLayer.AppLogic.Users;
using BusinessLogicLayer.AppLogic.Workers;
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
        services.Configure<JWTOptions>(
            configuration.GetSection("JWTOptions")
        );
        services.Configure<GeocodingOptions>(
            configuration.GetSection("Geocoding")
        );

        services.AddScoped<DB>(); // Zbog DI u repozitorije

        // Repozitoriji
        services.AddScoped<IUserRepository, UserRepository>();
        services.AddScoped<IWorkerRepository,WorkerRepository>();
        services.AddScoped<ISkillRepository, SkillRepository>();
        services.AddScoped<IJobRepository, JobRepository>();
        services.AddScoped<IPictureRepository, PictureRepository>();
        services.AddScoped<IReviewRepository, ReviewRepository>();
        services.AddScoped<IWorkingRepository, WorkingRepository>(); 
        services.AddScoped<IJobRoomRepository, JobRoomRepository>(); 
        // Servisi
        services.AddScoped<IJwtService, JwtService>();
        services.AddScoped<IUserService, UserService>();
        services.AddScoped<IWorkersService,WorkersService>();
        services.AddScoped<ISkillService, SkillService>();
        services.AddScoped<IJobService, JobService>();
        services.AddScoped<IReviewService, ReviewService>();
        services.AddScoped<IGeocodingService, GeocodingService>();
        services.AddScoped<IJobRoomService, JobRoomService>();

        return services;
    }
}
