package hr.foi.air.baufind.ws.model

data class JobRoom(
    val workingId: Int,
    val jobId: Int,
    val jobTitle: String,
    var jobStatus: String,
    val allowWorkerInvite: Boolean,
    val employerId: Int,
    val skillId: Int,
    val skillTitle: String,
    val workerId: Int?,
    val workerName: String,
    val workingStatus: String
)
