package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.request.EmployerReviewRequest
import hr.foi.air.baufind.ws.request.WorkerReviewRequest
import hr.foi.air.baufind.ws.response.GetUserReviewsResponse
import hr.foi.air.baufind.ws.response.ReviewNotificationResponse
import hr.foi.air.baufind.ws.response.ReviewResponse
import hr.foi.air.baufind.ws.response.SubmitReviewResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewService {
    @GET("review/{userId}")
    suspend fun getUserReviews(@Path("userId") userId: Int): GetUserReviewsResponse
    @POST("review/employer")
    suspend fun submitEmployerReview(@Body request: EmployerReviewRequest): SubmitReviewResponse
    @POST("review/worker")
    suspend fun submitWorkerReview(@Body request: WorkerReviewRequest): SubmitReviewResponse
    @GET("review/notifications")
    suspend fun getReviewNotifications(): List<ReviewNotificationResponse>

}