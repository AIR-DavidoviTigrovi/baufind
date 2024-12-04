package hr.foi.air.baufind.ws.response

data class GetAllSkillsResponse(
    val skills: List<SkillResponse>,
    val error: String? = null
)
