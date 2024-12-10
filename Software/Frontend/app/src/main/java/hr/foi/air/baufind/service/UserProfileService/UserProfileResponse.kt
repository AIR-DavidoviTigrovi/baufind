package hr.foi.air.baufind.service.UserProfileService

import hr.foi.air.baufind.ws.model.Skill

data class UserProfileResponse(
    val name: String,
    val email: String,
    val phone: String?,
    val address: String?,
    val profilePicture: String?,
    val joined: String,
    val skills: List<Skill>?,
    val reviews: ReviewResponse?
)
data class ReviewResponse(
    val averageRating: Double,
    val totalReviews: Int,
    val ratings: List<Int>
)