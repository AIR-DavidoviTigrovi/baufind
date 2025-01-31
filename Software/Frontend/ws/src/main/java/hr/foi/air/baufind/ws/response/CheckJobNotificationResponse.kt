package hr.foi.air.baufind.ws.response

import hr.foi.air.baufind.ws.model.Skill

data class CheckJobNotificationResponse(
    val jobs: List<JobNotificationResponse>?,
    var error : String?
)

data class JobNotificationResponse(
    val id: Int,
    val employer_id: Int,
    val job_status_id: Int,
    val title: String,
    val description: String,
    val allow_worker_invite: Boolean,
    val location: String,
    val lat: Double,
    val lng: Double,
    val pictures: List<String>,
    val skills: List<Skill>,
    val working_status_id: Int,
    val skill_id: Int
)
