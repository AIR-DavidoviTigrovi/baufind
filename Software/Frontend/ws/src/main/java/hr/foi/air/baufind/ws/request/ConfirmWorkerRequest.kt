package hr.foi.air.baufind.ws.request

data class ConfirmWorkerRequest(
    val workerId: Int,
    val jobId: Int,
    val skillId: Int,
    val workingStatusId: Int
)