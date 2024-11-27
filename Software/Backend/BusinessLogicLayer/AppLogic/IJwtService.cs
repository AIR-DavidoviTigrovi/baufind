using BusinessLogicLayer.AppLogic.Users;

namespace BusinessLogicLayer.AppLogic;

/// <summary>
/// Servis za generiranje, provjeravanje i korištenje JWT-ova
/// </summary>
public interface IJwtService
{
    /// <summary>
    /// Vraća JWT token za korisnika
    /// </summary>
    /// <param name="user">korisnik</param>
    /// <returns>JWT</returns>
    public string GenerateToken(UserRecord user);
}
