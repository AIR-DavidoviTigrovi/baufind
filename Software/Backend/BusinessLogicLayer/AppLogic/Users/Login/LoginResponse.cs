namespace BusinessLogicLayer.AppLogic.Users.Login;

/// <summary>
/// Odgovor API-ja na uspješnu prijavu
/// </summary>
public record LoginResponse
{
    public string? Success { get; set; } = string.Empty;
    public string? JWT { get; set; } = null;
    public string? Error { get; set; } = string.Empty;
}
