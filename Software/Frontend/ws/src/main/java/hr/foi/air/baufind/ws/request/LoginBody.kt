package hr.foi.air.baufind.ws.request

data class LoginBody(
    val email : String,
    val password : String,
    val firebaseToken : String? = null
)
