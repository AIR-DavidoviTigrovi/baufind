package hr.foi.air.baufind.ws.response

data class GetAllSkillsResponse(
    val skills: List<SkillsWithIdResponse>,
    val error: String? = null
)
