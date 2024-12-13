package hr.foi.air.baufind.ws.response

data class UpdateUserResponse(
    val success: Boolean,
    val message: String? = null,
    val errors: List<String>? = null
)
