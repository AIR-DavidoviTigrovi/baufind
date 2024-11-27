namespace BusinessLogicLayer;

/// <summary>
/// Klasa za ubacivanje JWT ključa, issuer i audience
/// Pogledati: https://learn.microsoft.com/en-us/aspnet/core/fundamentals/configuration/options?view=aspnetcore-9.0
/// </summary>
public class JWTOptions
{
    public string Key { get; set; } = string.Empty;
    public string Issuer { get; set; } = string.Empty;
    public string Audience { get; set; } = string.Empty;
}
