using BusinessLogicLayer.AppLogic.Geocoding.ReverseGeocoding;

namespace BusinessLogicLayer.AppLogic.Geocoding;

/// <summary>
/// Servis za geocoding i reverse geocoding
/// </summary>
public interface IGeocodingService
{
    public Task<ReverseGeocodingResponse> ReverseGeocode(double latitude, double longitude);
}
