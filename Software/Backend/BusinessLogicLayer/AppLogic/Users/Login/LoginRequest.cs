namespace BusinessLogicLayer.AppLogic.Users.Login;

/// <summary>
/// Ovi podaci se šalju kod prijave korisnika
/// </summary>
public record LoginRequest
{
    public string Email { get; set; }
    public string Password { get; set; }
    public string? FirebaseToken { get; set; } = null;
}
