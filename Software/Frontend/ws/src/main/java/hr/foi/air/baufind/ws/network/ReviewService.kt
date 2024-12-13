package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.response.GetUserReviewsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ReviewService {
    @GET("review/{userId}")
    suspend fun getUserReviews(@Path("userId") userId: Int): GetUserReviewsResponse
}