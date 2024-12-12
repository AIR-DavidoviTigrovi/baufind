package hr.foi.air.baufind.service.ReviewService

import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.response.GetUserReviewsResponse

class ReviewService(private val tokenProvider: TokenProvider) {
    val service = NetworkService.createReviewService(tokenProvider)
    suspend fun getUserReviews(userId: Int): GetUserReviewsResponse? {
        return try {
            service.getUserReviews(userId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}