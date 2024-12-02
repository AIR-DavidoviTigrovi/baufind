package hr.foi.air.baufind.ws.model

data class Worker(
    val id: Int,
    val name: String,
    val address: String,
    val numOfJobs: Int,
    val skills: String,
    val avgRating: Double
)