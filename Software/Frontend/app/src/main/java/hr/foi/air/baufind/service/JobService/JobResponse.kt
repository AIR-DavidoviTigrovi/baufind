package hr.foi.air.baufind.service.JobService

data class JobResponse(
    val added: Boolean,
    val message: String,
    val jobId: Int?
)
