using System.Text.Json.Serialization;

namespace BusinessLogicLayer.AppLogic.Geocoding;

/// <summary>
/// Odgovor sa Geocoding API-ja kojeg aplikacija dobiva
/// </summary>
internal record ReverseGeocodingApiResult
{
    [JsonPropertyName("place_id")]
    public long PlaceId { get; set; }

    [JsonPropertyName("licence")]
    public string Licence { get; set; }

    [JsonPropertyName("osm_type")]
    public string OsmType { get; set; }

    [JsonPropertyName("osm_id")]
    public long OsmId { get; set; }

    [JsonPropertyName("lat")]
    public string Lat { get; set; }

    [JsonPropertyName("lon")]
    public string Lon { get; set; }

    [JsonPropertyName("display_name")]
    public string DisplayName { get; set; }

    [JsonPropertyName("address")]
    public ReverseGeocodingApiAddressResult Address { get; set; }

    [JsonPropertyName("boundingbox")]
    public string[] BoundingBox { get; set; }
}

/// <summary>
/// Informacije o adresi dobivene od strane API-ja
/// </summary>
internal record ReverseGeocodingApiAddressResult
{
    [JsonPropertyName("amenity")]
    public string? Amenity { get; set; }

    [JsonPropertyName("house_number")]
    public string? HouseNumber { get; set; }

    [JsonPropertyName("road")]
    public string Road { get; set; }

    [JsonPropertyName("quarter")]
    public string Quarter { get; set; }

    [JsonPropertyName("neighbourhood")]
    public string? Neighbourhood { get; set; }

    [JsonPropertyName("suburb")]
    public string Suburb { get; set; } 

    [JsonPropertyName("city_district")]
    public string CityDistrict { get; set; }

    [JsonPropertyName("town")]
    public string? Town { get; set; }

    [JsonPropertyName("municipality")]
    public string? Municipality { get; set; }

    [JsonPropertyName("county")]
    public string County { get; set; }

    [JsonPropertyName("ISO3166-2-lvl6")]
    public string ISO { get; set; }

    [JsonPropertyName("postcode")]
    public string Postcode { get; set; }

    [JsonPropertyName("country")]
    public string Country { get; set; }

    [JsonPropertyName("country_code")]
    public string CountryCode { get; set; }
}