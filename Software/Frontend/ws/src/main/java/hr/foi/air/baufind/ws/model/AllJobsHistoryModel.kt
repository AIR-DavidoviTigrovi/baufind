package hr.foi.air.baufind.ws.model

data class AllJobsHistoryModel(
    val jobId: Int,
    val title: String,
    val picture: ByteArray,
    val completionDate: String,
    val isOwner: Boolean
)
