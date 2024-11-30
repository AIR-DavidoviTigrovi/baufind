package hr.foi.air.baufind.ws.model

data class Worker(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val numOfJobs: Int,
    val skills: List<String>,
    val availability: String,
    val avgRating: Double
)