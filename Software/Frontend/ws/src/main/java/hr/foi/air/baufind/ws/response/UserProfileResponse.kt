package hr.foi.air.baufind.ws.response

data class UserProfileResponse(
    val name: String,
    val email: String,
    val phone: String?,
    val address: String?,
    val profilePicture: String?,
    val skills: List<SkillResponse>?,
    val reviews: ReviewResponse?
)

data class SkillResponse(
    val id: Int,
    val title: String
)

data class ReviewResponse(
    val averageRating: Double,
    val totalReviews: Int,
    val ratings: List<Int>
)
