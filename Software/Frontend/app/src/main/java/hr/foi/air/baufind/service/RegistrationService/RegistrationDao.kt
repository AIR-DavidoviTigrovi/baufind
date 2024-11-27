package hr.foi.air.baufind.service.RegistrationService

data class RegistrationDao(
    val name : String,
    val email : String,
    val phone : String,
    val address : String,
    val password : String,
    val confirmPassword : String
)