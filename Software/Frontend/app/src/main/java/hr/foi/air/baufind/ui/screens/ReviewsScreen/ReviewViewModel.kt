package hr.foi.air.baufind.ui.screens.ReviewsScreen

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.baufind.service.ReviewService.ReviewService
import hr.foi.air.baufind.ws.model.Image
import hr.foi.air.baufind.ws.request.EmployerReviewRequest
import hr.foi.air.baufind.ws.request.WorkerReviewRequest
import kotlinx.coroutines.launch

class ReviewViewModel(private val reviewService: ReviewService) : ViewModel() {
    val selectedImages = mutableListOf<Uri>()

    fun submitEmployerReview(
        jobName: String,
        rating: Int,
        comment: String,
        context: Context
    ) {
        viewModelScope.launch {
            val reviewerId = getCurrentUserId(context) // Replace with your implementation
            val jobId = getJobIdByName(jobName) // Replace with your implementation

            val imageList = selectedImages.map { uri ->
                // Convert URIs to your Image model
                Image(uri.toString())
            }

            val request = EmployerReviewRequest(
                reviewerId = reviewerId,
                jobId = jobId,
                comment = comment,
                rating = rating,
                images = imageList
            )

            val response = reviewService.submitEmployerReview(request)

            // Handle the response (e.g., show a toast or update UI)
            if (response?.success == true) {
                // Success logic here
            } else {
                // Failure logic here
            }
        }
    }

    // Similarly, implement submitWorkerReview for workers
    fun submitWorkerReview(
        workingId: Int,
        rating: Int,
        comment: String,
        context: Context
    ) {
        viewModelScope.launch {
            val imageList = selectedImages.map { uri ->
                Image(uri.toString())
            }

            val request = WorkerReviewRequest(
                workingId = workingId,
                comment = comment,
                rating = rating,
                images = imageList
            )

            val response = reviewService.submitWorkerReview(request)

            // Handle the response
            if (response?.success == true) {
                // Success logic
            } else {
                // Failure logic
            }
        }
    }

    private fun getCurrentUserId(context: Context): Int {
        // Fetch the current user ID from shared preferences or session
        return 123 // Placeholder
    }

    private fun getJobIdByName(jobName: String): Int {
        // Fetch the job ID by its name (e.g., from a database or API)
        return 456 // Placeholder
    }
}