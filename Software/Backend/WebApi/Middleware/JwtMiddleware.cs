using Microsoft.AspNetCore.Authorization;
using System.Security.Claims;

namespace WebApi.Middleware;

/// <summary>
/// Middleware koji vadi korisnikov ID iz JWT-a za korištenje na API endpointima
/// </summary>
public class JwtMiddleware
{
    private readonly RequestDelegate _next;

    public JwtMiddleware(RequestDelegate next)
    {
        _next = next;
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

        context.Items["UserId"] = userId;

        await _next(context);
    }
}
