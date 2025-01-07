package hr.foi.air.baufind.ws.request

data class CallForWorkingBody(
    val workerId: Int,
    val jobId: Int,
    val skillId: Int
)
