package hr.foi.air.baufind.service.AuthService

data class LogoutResponse(
    val successfulLogin : Boolean,
    val message : String,
)