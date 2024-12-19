using BusinessLogicLayer.AppLogic.Geocoding;
using BusinessLogicLayer.AppLogic.Geocoding.ReverseGeocoding;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace WebApi.Controllers;

[ApiController]
[Route("geocoding")]
public class GeocodingController : ControllerBase
{
    private readonly IGeocodingService _service;

    public GeocodingController(IGeocodingService service)
    {
        _service = service;
    }

    [HttpGet]
    [Authorize]
    public async Task<ActionResult<ReverseGeocodingResponse>> ReverseGeocode([FromQuery] double? lat, [FromQuery] double? lng)
    {
        if (lat == null || lng == null)
        {
            return BadRequest(new ReverseGeocodingResponse
            {
                Error = "Nisu prenesene obje koordinate."
            });
        }

        var response = await _service.ReverseGeocode(lat.Value, lng.Value);

        if (!string.IsNullOrEmpty(response.Error))
        {
            return NotFound(response);
        }

        return response;
    }
}
