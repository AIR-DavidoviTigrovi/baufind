package hr.foi.air.baufind.ws.response

import hr.foi.air.baufind.ws.model.User

data class RegistrationResponse(
    val success : String,
    val user : User,
    val error : String
)
