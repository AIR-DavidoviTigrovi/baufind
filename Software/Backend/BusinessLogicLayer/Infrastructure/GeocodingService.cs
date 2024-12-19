using BusinessLogicLayer.AppLogic;
using BusinessLogicLayer.AppLogic.Geocoding;
using BusinessLogicLayer.AppLogic.Geocoding.ReverseGeocoding;
using Microsoft.Extensions.Options;
using System.Net.Http.Headers;
using System.Text.Json;

namespace BusinessLogicLayer.Infrastructure;

/// <summary>
/// Implementacija servisa za geocoding i reverse geocoding
/// </summary>
public class GeocodingService : IGeocodingService
{
    private const string _uri = "https://geocode.maps.co/reverse";
    private readonly string _apiKey;

    public GeocodingService(IOptions<GeocodingOptions> options)
    {
        _apiKey = options.Value.ApiKey;
    }

    public async Task<ReverseGeocodingResponse> ReverseGeocode(double latitude, double longitude)
    {
        using (var client = CreateConfiguredHttpClient())
        {
            var uri = new Uri($"{_uri}?lat={latitude}&lon={longitude}&api_key={_apiKey}");
            var response = await client.GetAsync(uri);
            if (!response.IsSuccessStatusCode)
            {
                return new ReverseGeocodingResponse()
                {
                    Error = "Greška kod dohvata odgovora."
                };
            }
            else
            {
                return await ExtractContentFromResponse(response.Content);
            }
        }
    }

    private HttpClient CreateConfiguredHttpClient()
    {
        var client = new HttpClient();
        client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
        return client;
    }

    private async Task<ReverseGeocodingResponse> ExtractContentFromResponse(HttpContent content)
    {
        var contentString = await content.ReadAsStringAsync();
        try
        {
            var responseObject = JsonSerializer.Deserialize<ReverseGeocodingApiResult>(contentString);
            if (responseObject == null)
            {
                return new ReverseGeocodingResponse
                {
                    Error = "Nije deserijaliziran odgovor."
                };
            }

            return new ReverseGeocodingResponse
            {
                Location = responseObject.DisplayName,
                Amenity = responseObject.Address?.Amenity ?? null,
                HouseNumber = responseObject.Address?.HouseNumber ?? null,
                Street = responseObject.Address?.Road ?? null,
                Quarter = responseObject.Address?.Quarter ?? null,
                Neighbourhood = responseObject.Address?.Neighbourhood ?? null,
                Suburb = responseObject.Address?.Suburb ?? null,
                CityDistrict = responseObject.Address?.CityDistrict ?? null,
                Town = responseObject.Address?.Town ?? null,
                Municipality = responseObject.Address?.Municipality ?? null,
                County = responseObject.Address?.County ?? null,
                Postcode = responseObject.Address?.Postcode ?? null,
                Country = responseObject.Address?.Country ?? null
            };
        }
        catch (Exception ex)
        {
            return new ReverseGeocodingResponse
            {
                Error = "Greška kod deserijalizacije odgovora."
            };
        }
    }
}
