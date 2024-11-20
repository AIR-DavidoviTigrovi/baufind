namespace BusinessLogicLayer.AppLogic.Users.RegisterUser;

/// <summary>
/// Odgovor API-ja na registraciju novog korisnika
/// </summary>
public record RegisterUserResponse
{
    public string? Success { get; set; } = string.Empty;
    public UserRecord? User { get; set; } = null;
    public string? Error { get; set; } = string.Empty;
}
