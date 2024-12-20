namespace BusinessLogicLayer.AppLogic.Geocoding.ReverseGeocoding;

/// <summary>
/// Odgovor što naša aplikacija šalje na putanji
/// </summary>
public record ReverseGeocodingResponse
{
    /// <summary>
    /// Greška kod dohvaćanja
    /// </summary>
    public string? Error { get; set; } = string.Empty;

    /// <summary>
    /// Puni tekst lokacije
    /// Prepisano iz odgovora API-ja "display_name"
    /// </summary>
    public string? Location { get; set; }

    /// <summary>
    /// Prepisano iz odgovora API-ja "amenity"
    /// </summary>
    public string? Amenity { get; set; }

    /// <summary>
    /// Kućni broj
    /// Prepisano iz odgovora API-ja "house_number"
    /// </summary>
    public string? HouseNumber { get; set; }

    /// <summary>
    /// Ulica
    /// Prepisano iz odgovora API-ja "road"
    /// </summary>
    public string? Street { get; set; }

    /// <summary>
    /// Prepisano iz odgovora API-ja "quarter"
    /// </summary>
    public string? Quarter { get; set; }

    /// <summary>
    /// Prepisano iz odgovora API-ja "neighbourhood"
    /// </summary>
    public string? Neighbourhood { get; set; }

    /// <summary>
    /// Prepisano iz odgovora API-ja "suburb"
    /// </summary>
    public string? Suburb { get; set; }

    /// <summary>
    /// Kratko ime grada
    /// Prepisano iz odgovora API-ja "city_district"
    /// </summary>
    public string? CityDistrict { get; set; }

    /// <summary>
    /// Dugo ime grada
    /// Prepisano iz odgovora API-ja "town"
    /// </summary>
    public string? Town { get; set; }

    /// <summary>
    /// Dugo ime grada
    /// Prepisano iz odgovora API-ja "municipality"
    /// </summary>
    public string? Municipality { get; set; }

    /// <summary>
    /// Županija
    /// Prepisano iz odgovora API-ja "county"
    /// </summary>
    public string? County { get; set; }

    /// <summary>
    /// Poštanski broj
    /// Prepisano iz odgovora API-ja "postcode"
    /// </summary>
    public string? Postcode { get; set; }

    /// <summary>
    /// Država
    /// Prepisano iz odgovora API-ja "country"
    /// </summary>
    public string? Country { get; set; }
}
