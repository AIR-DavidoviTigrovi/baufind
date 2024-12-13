package hr.foi.air.baufind.ws.response

import hr.foi.air.baufind.ws.model.Worker

data class WorkersSkillResponse(
    val workerRecords: List<Worker>,
    val error: String
)
