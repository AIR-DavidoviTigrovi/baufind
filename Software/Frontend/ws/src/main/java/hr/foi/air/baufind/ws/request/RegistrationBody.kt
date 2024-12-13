package hr.foi.air.baufind.ws.request

data class RegistrationBody(
    val name : String,
    val email : String,
    val phone : String,
    val address : String,
    val password : String,
    val repeatPassword : String
)
