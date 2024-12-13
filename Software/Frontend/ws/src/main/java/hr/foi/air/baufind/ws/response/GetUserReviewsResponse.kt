package hr.foi.air.baufind.ws.response

import hr.foi.air.baufind.ws.model.Review

data class GetUserReviewsResponse(
    val workerReviews: List<Review>? = null,
    val employerReviews: List<Review>? = null,
    val error: String? = null
)
