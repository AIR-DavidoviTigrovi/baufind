package hr.foi.air.baufind.service.JobHistoryService

import hr.foi.air.baufind.ws.model.AllJobsHistoryModel

data class FullJobHistoryResponse(
    val success: Boolean,
    val message: String?,
    val jobs: List<AllJobsHistoryModel>
)
