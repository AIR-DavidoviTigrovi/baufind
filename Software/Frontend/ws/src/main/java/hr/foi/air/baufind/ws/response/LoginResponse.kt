package hr.foi.air.baufind.ws.response

data class LoginResponse(
    val success : String,
    val jwt : String,
    val error : String
)
