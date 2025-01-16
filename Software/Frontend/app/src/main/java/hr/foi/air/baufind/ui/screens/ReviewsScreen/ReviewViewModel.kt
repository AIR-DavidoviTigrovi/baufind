package hr.foi.air.baufind.ui.screens.ReviewsScreen

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.baufind.service.ReviewService.ReviewService
import hr.foi.air.baufind.ws.model.Image
import hr.foi.air.baufind.ws.request.EmployerReviewRequest
import hr.foi.air.baufind.ws.request.WorkerReviewRequest
import kotlinx.coroutines.launch
import java.io.InputStream


class ReviewViewModel(private val reviewService: ReviewService) : ViewModel() {
    val selectedImages = mutableStateListOf<Uri>()

    private fun getBytesFromUri(context: Context, uri: Uri): ByteArray? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream: InputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun submitEmployerReview(
        jobId: Int,
        rating: Int,
        comment: String,
        context: Context
    ) {
        viewModelScope.launch {

            val imageList = selectedImages.mapNotNull { uri ->
                getBytesFromUri(context, uri)?.let { bytes ->
                    val base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)
                    Image(
                        picture = base64String
                    )
                }
            }

            val request = EmployerReviewRequest(
                jobId = jobId,
                comment = comment,
                rating = rating,
                images = imageList
            )

            val response = reviewService.submitEmployerReview(request)
            Log.d("submitEmployerReview", "Response: $response")
        }
    }

    fun submitWorkerReview(
        workingId: Int,
        rating: Int,
        comment: String,
        context: Context
    ) {
        viewModelScope.launch {

            val imageList = selectedImages.mapNotNull { uri ->
                getBytesFromUri(context, uri)?.let { bytes ->
                    val base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)
                    Image(
                        picture = base64String
                    )
                }
            }

            val request = WorkerReviewRequest(
                workingId = workingId,
                comment = comment,
                rating = rating,
                images = imageList
            )

            val response = reviewService.submitWorkerReview(request)
            Log.d("submitWorkerReview", "Response: $response")
        }
    }
}