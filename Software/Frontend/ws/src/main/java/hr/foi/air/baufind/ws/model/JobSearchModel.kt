package hr.foi.air.baufind.ws.model

data class JobSearchModel(
    val id: Int,
    val employerId: Int,
    val jobStatusId: Int,
    val title: String,
    val description: String,
    val allowWorkerInvite: Boolean,
    val location: String,
    val lat: Double?,
    val lng: Double?,
    val pictures: List<ByteArray>,
    val skills: List<Skill>
)
