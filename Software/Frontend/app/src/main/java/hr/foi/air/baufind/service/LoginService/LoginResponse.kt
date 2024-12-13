package hr.foi.air.baufind.service.LoginService

data class LoginResponse(
    val successfulLogin : Boolean,
    val message : String,
    val jwt : String
)
