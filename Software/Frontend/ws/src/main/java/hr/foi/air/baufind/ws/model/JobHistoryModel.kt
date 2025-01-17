package hr.foi.air.baufind.ws.model

data class JobHistoryModel(
    val id: Int,
    val title: String,
    val description: String,
    val location: String,
    val ownerName: String,
    val workers: List<WorkerOnJobModel>,
    val events: List<EventModel>
)
