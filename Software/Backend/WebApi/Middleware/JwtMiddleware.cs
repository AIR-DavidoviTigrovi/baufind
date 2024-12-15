using BusinessLogicLayer.AppLogic.Users;
using BusinessLogicLayer.Infrastructure;
using DataAccessLayer.AppLogic;
using Microsoft.AspNetCore.Authorization;
using System.Security.Claims;

namespace WebApi.Middleware;

/// <summary>
/// Middleware koji vadi korisnikov ID iz JWT-a za korištenje na API endpointima
/// </summary>
public class JwtMiddleware
{
    private readonly RequestDelegate _next;
    private readonly IServiceScopeFactory _scopeFactory; 
    public JwtMiddleware(RequestDelegate next, IServiceScopeFactory scopeFactory)
    {
        _next = next;
        _scopeFactory = scopeFactory;
    }

    public async Task Invoke(HttpContext context)
    {
        var endpoint = context.GetEndpoint();
        if (endpoint?.Metadata?.GetMetadata<AllowAnonymousAttribute>() != null)
        {
            await _next(context);
            return;
        }

        var userIdClaim = context.User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

        if (userIdClaim == null || !int.TryParse(userIdClaim, out var userId))
        {
            context.Response.StatusCode = StatusCodes.Status401Unauthorized;
            await context.Response.WriteAsync("Nemate pristup.");
            return;
        }
        using (var scope = _scopeFactory.CreateScope())
        {
            var userService = scope.ServiceProvider.GetRequiredService<IUserService>();
            var user = userService.GetOneUser(userId);

            if (user == null || user.User.Deleted)
            {
                context.Response.StatusCode = StatusCodes.Status403Forbidden;
                await context.Response.WriteAsync("Vaš račun je obrisan ili ne postoji.");
                return;
            }

            context.Items["UserId"] = userId;
        }

        await _next(context);
    }
}
