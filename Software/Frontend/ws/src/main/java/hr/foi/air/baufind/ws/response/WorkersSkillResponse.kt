package hr.foi.air.baufind.ws.response

import hr.foi.air.baufind.ws.model.Worker

data class WorkersSkillResponse(
    val success: String,
    val error: String,
    val data: List<Worker>

)
