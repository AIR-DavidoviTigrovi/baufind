package hr.foi.air.baufind.service.WorkerService

data class CallForWorkingRequest(
    val workerId: Int,
    val jobId: Int,
    val skillId: Int
)
