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
                Amenity = responseObject.Address.Amenity,
                HouseNumber = responseObject.Address.HouseNumber,
                Street = responseObject.Address.Road,
                Quarter = responseObject.Address.Quarter,
                Neighbourhood = responseObject.Address.Neighbourhood,
                Suburb = responseObject.Address.Suburb,
                CityDistrict = responseObject.Address.CityDistrict,
                Town = responseObject.Address.Town,
                Municipality = responseObject.Address.Municipality,
                County = responseObject.Address.County,
                Postcode = responseObject.Address.Postcode,
                Country = responseObject.Address.Country
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
