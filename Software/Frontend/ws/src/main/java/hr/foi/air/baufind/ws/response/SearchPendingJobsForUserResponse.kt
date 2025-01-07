package hr.foi.air.baufind.ws.response

import hr.foi.air.baufind.ws.model.JobWorkingModel

data class SearchPendingJobsForUserResponse(
    val success: Boolean,
    val jobs : List<JobWorkingModel>,
    val error : String?
)