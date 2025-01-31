package hr.foi.air.baufind.service.JobGetService

import hr.foi.air.baufind.ws.model.FullJobModel

data class JobGetResponse(
    val success: Boolean,
    val message: String?,
    val job: FullJobModel?
)
