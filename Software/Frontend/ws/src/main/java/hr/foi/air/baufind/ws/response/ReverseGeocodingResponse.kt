package hr.foi.air.baufind.ws.response

data class ReverseGeocodingResponse(
    val error: String?,
    val location: String?,
    val amenity: String?,
    val houseNumber: String?,
    val street: String?,
    val quarter: String?,
    val neighbourhood: String?,
    val suburb: String?,
    val cityDistrict: String?,
    val town: String?,
    val municipality: String?,
    val county: String?,
    val postcode: String?,
    val country: String?
)
