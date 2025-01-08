package hr.foi.air.baufind.service.JobService

import hr.foi.air.baufind.ws.model.MyJobModel

data class MyJobsForUserResponse (
    val success: Boolean,
    val error: String? = null,
    val jobs: List<MyJobModel> = listOf()
)