package hr.foi.air.baufind.core.map.models

data class LocationInformation(
    var lat: Double,
    var long: Double,
    var location: String = "",
    var isValid: Boolean = false
)
