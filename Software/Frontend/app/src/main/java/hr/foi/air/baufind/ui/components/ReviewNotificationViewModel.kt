package hr.foi.air.baufind.ui.components

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import hr.foi.air.baufind.ws.response.ReviewNotificationResponse

class ReviewNotificationViewModel : ViewModel() {

    private val _reviewNotifications = mutableStateListOf<ReviewNotificationResponse>()
    val reviewNotifications: List<ReviewNotificationResponse> get() = _reviewNotifications

    fun updateReviewNotifications(notifications: List<ReviewNotificationResponse>) {
        _reviewNotifications.clear()
        _reviewNotifications.addAll(notifications)
    }
}