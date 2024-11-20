namespace BusinessLogicLayer.AppLogic.Users.RegisterUser;

/// <summary>
/// Odgovor API-ja na registraciju novog korisnika
/// </summary>
public class RegisterUserResponse
{
    public string? Success { get; set; } = string.Empty;
    public string? Error { get; set; } = string.Empty;
}
