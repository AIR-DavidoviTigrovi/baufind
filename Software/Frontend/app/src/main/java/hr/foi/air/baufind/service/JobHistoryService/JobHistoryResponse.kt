package hr.foi.air.baufind.service.JobHistoryService

import hr.foi.air.baufind.ws.model.JobHistoryModel

data class JobHistoryResponse(
    val success: Boolean,
    val message: String?,
    val jobHistory: JobHistoryModel?
)
