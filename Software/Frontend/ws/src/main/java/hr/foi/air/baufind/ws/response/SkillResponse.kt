package hr.foi.air.baufind.ws.response

import hr.foi.air.baufind.ws.model.Skill

data class SkillResponse(
    val skills: List<Skill>,
    val error: String
)