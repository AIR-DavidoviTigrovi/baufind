package hr.foi.air.baufind.service.ReviewService

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.foi.air.baufind.ui.screens.ReviewsScreen.ReviewViewModel

class ReviewViewModelFactory(private val reviewService: ReviewService) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            return ReviewViewModel(reviewService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}