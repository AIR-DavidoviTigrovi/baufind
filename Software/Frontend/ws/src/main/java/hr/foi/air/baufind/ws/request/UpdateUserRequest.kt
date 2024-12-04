package hr.foi.air.baufind.ws.request

data class UpdateUserRequest(
    val userId : Int,
    val name: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val profilePicture: String? = null,
    val addSkills: List<Int> = emptyList(),
    val removeSkills: List<Int> = emptyList()
)
