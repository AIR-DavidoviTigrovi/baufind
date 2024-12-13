namespace DataAccessLayer;

/// <summary>
/// Klasa za ubacivanje connection stringa za bazu koristeći ASP.NET Options pattern
/// Pogledati: https://learn.microsoft.com/en-us/aspnet/core/fundamentals/configuration/options?view=aspnetcore-9.0
/// </summary>
public class DBOptions
{
    public string ConnectionString { get; set; } = string.Empty;
}
