package hr.foi.air.baufind.service.ReviewService

import android.util.Log
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.request.EmployerReviewRequest
import hr.foi.air.baufind.ws.request.WorkerReviewRequest
import hr.foi.air.baufind.ws.response.GetUserReviewsResponse
import hr.foi.air.baufind.ws.response.ReviewNotificationResponse
import hr.foi.air.baufind.ws.response.SubmitReviewResponse

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
    suspend fun submitEmployerReview(request: EmployerReviewRequest): SubmitReviewResponse? {
        return try {
            Log.d("submitEmployerReview", "Request: $request")
            service.submitEmployerReview(request)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    suspend fun submitWorkerReview(request: WorkerReviewRequest): SubmitReviewResponse? {
        return try {
            Log.d("submitWorkerReview", "REquest: $request")
            service.submitWorkerReview(request)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    suspend fun getReviewNotifications(): List<ReviewNotificationResponse>? {
        return try {
            service.getReviewNotifications()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
