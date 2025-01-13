package hr.foi.air.baufind.ws.response

data class MyJobNotificationResponse(
    val notificationModels: List<MyJobNotificationModel>,
    val message: String
)

data class MyJobNotificationModel(
    val workerId: Int,
    val name: String,
    val address: String,
    val skillId: Int,
    val jobId: Int,
    val jobTitle: String,
    val workingStatusId: Int,
    val rating: Double,
    val skill: String,
    val completedJobsCount: Int
)
