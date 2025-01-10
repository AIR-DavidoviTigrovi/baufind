package hr.foi.air.baufind.ui.screens.ReviewsScreen

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ReviewViewModel : ViewModel() {
    val selectedImages = mutableStateListOf<Uri>()

    fun submitEmployerReview(jobName: String, rating: Int, comment: String, context: Context) {
    }
}
