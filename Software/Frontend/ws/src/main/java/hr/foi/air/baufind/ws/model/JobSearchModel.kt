package hr.foi.air.baufind.ws.model

data class JobSearchModel(
    val id: Int,
    val employer_id: Int,
    val job_status_id: Int,
    val title: String,
    val description: String,
    val allow_worker_invite: Boolean,
    val location: String,
    val lat: Double?,
    val lng: Double?,
    val pictures: List<String>,
    val skills: List<Skill>
)
