package hr.foi.air.baufind.ws.model

import java.util.Date


data class Review (
    val reviewId: Int,
    val reviewerId: Int,
    val reviewerName: String,
    val reviewerImage: String? = null,
    val reviewedJobId: Int,
    val jobTitle: String,
    val comment: String,
    val rating: Int,
    val reviewDate: String,
    val pictures: List<Image> = emptyList()
)