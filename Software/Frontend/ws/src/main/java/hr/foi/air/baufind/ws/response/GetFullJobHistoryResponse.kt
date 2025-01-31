package hr.foi.air.baufind.ws.response

import hr.foi.air.baufind.ws.model.AllJobsHistoryModel

data class GetFullJobHistoryResponse(
    val jobs: List<AllJobsHistoryModel>,
    val error: String?
)
