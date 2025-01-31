package hr.foi.air.baufind.ws.model

data class JobWorkingModel (
    val jobId: Int,
    val workingId: Int,
    val workerId: Int?,
    val statusId: Int,
    val status: String,
    val title: String,
    val location: String
)