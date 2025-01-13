package hr.foi.air.baufind.ui.screens.NotificationsScreen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.baufind.service.JobService.JobService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.response.JobNotificationResponse
import kotlinx.coroutines.launch

class JobNotificationViewModel : ViewModel() {
    var isLoading = mutableStateOf(false)
    var jobs = mutableStateListOf<JobNotificationResponse>()
    var tokenProvider: TokenProvider? = null
        set(value) {
            field = value
            value?.let { fetchJobNotifications(it) }
        }

    private fun fetchJobNotifications(tokenProvider: TokenProvider) {
        viewModelScope.launch {
            isLoading.value = true
            val service = JobService()
            val response = service.checkJobNotifications(tokenProvider)

            if (response.isNullOrEmpty()) {
                Log.e("JobNotification", "Response is null or empty")
                jobs.clear()
            } else {
                Log.d("JobNotification", "Response: $response")
                jobs.clear()
                jobs.addAll(response)
            }

            isLoading.value = false
        }
    }
}



