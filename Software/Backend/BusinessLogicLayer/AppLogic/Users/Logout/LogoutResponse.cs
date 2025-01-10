namespace BusinessLogicLayer.AppLogic.Users.Logout;

/// <summary>
/// Odgovor na odjavu
/// </summary>
public record LogoutResponse
{
    public string? Success { get; set; } = string.Empty;
    public string? Error { get; set; } = string.Empty;
}
