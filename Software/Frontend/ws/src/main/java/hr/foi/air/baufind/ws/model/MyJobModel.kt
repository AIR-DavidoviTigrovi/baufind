package hr.foi.air.baufind.ws.model

data class MyJobModel(
    val id: Int,
    val employerId: Int,
    val jobStatusId: Int,
    val jobStatus: String,
    val title: String,
    val description: String,
    val allowWorkerInvite: Boolean,
    val location: String,
    val lat: Double?,
    val lng: Double?,
    val userIsEmployer: Boolean
)