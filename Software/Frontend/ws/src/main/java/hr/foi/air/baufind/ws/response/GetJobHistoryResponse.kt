package hr.foi.air.baufind.ws.response

import hr.foi.air.baufind.ws.model.JobHistoryModel

data class GetJobHistoryResponse(
    val jobHistory: JobHistoryModel,
    val error: String?
)
