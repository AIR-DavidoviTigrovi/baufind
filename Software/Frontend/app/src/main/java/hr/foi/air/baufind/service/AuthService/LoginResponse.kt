package hr.foi.air.baufind.service.AuthService

data class LoginResponse(
    val successfulLogin : Boolean,
    val message : String,
    val jwt : String
)
