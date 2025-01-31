package hr.foi.air.baufind.core.map.models

data class Coordinates(
    var lat: Double,
    var long: Double,
    var isValid: Boolean = false
)
