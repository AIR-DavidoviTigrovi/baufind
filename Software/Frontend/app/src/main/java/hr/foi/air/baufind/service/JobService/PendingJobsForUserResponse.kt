package hr.foi.air.baufind.service.JobService

import hr.foi.air.baufind.ws.model.JobWorkingModel

data class PendingJobsForUserResponse(
    val success: Boolean,
    val error: String? = null,
    val jobs: List<JobWorkingModel> = listOf()
)