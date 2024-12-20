namespace BusinessLogicLayer.AppLogic;

/// <summary>
/// Klasa za ubacivanje Geocoding API-ja
/// Pogledati: https://learn.microsoft.com/en-us/aspnet/core/fundamentals/configuration/options?view=aspnetcore-9.0
/// </summary>
public class GeocodingOptions
{
    public string ApiKey { get; set; } = string.Empty;
}
