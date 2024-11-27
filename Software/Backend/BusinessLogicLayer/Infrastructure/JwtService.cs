using BusinessLogicLayer.AppLogic;
using BusinessLogicLayer.AppLogic.Users;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace BusinessLogicLayer.Infrastructure;

/// <summary>
/// Implementacija servisa za generiranje, provjeravanje i korištenje JWT-ova
/// </summary>
public class JwtService : IJwtService
{
    private readonly byte[] _keyBytes;
    private readonly string _issuer;
    private readonly string _audience;

    public JwtService(IOptions<JWTOptions> options)
    {
        _keyBytes = Encoding.ASCII.GetBytes(options.Value.Key);
        _issuer = options.Value.Issuer;
        _audience = options.Value.Audience;
    }

    /// <summary>
    /// Vraća JWT token za korisnika
    /// </summary>
    /// <param name="user">korisnik</param>
    /// <returns>JWT</returns>
    public string GenerateToken(UserRecord user)
    {
        var tokenHandler = new JwtSecurityTokenHandler();

        var claims = new List<Claim>
        {
            new(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
            new(JwtRegisteredClaimNames.Sub, user.Id.ToString()),
            new(JwtRegisteredClaimNames.Email, user.Email)
        };

        var tokenDescriptor = new SecurityTokenDescriptor
        {
            Subject = new ClaimsIdentity(claims),
            Expires = DateTime.UtcNow.AddMinutes(30),
            Issuer = _issuer,
            Audience = _audience,
            SigningCredentials = new SigningCredentials(new SymmetricSecurityKey(_keyBytes), SecurityAlgorithms.HmacSha256Signature)
        };

        var token = tokenHandler.CreateToken(tokenDescriptor);
        return tokenHandler.WriteToken(token);
    }
}
