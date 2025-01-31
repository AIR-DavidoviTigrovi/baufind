package hr.foi.air.baufind.ws.response

data class ReviewNotificationResponse (
    val jobId: Int,
    val jobTitle: String,
    val personId: Int,
    val personName: String,
    val position: String,
    val workingId: Int
)