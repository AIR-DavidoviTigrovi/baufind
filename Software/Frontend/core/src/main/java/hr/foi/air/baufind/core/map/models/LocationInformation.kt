package hr.foi.air.baufind.core.map.models

data class LocationInformation(
    var lat: Double,
    var long: Double,
    var isValid: Boolean = false
)
