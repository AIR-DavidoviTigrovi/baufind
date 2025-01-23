package hr.foi.air.baufind.ui.screens.MyJobsScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.baufind.service.JobService.JobService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.request.ConfirmWorkerRequest
import hr.foi.air.baufind.ws.response.MyJobNotificationModel
import kotlinx.coroutines.launch

class MyJobsNotificationsViewModel : ViewModel(){
    var success: Boolean = false
    var notifications = mutableStateOf(emptyList<MyJobNotificationModel>())
    var isLoading = mutableStateOf(false)
    var message = mutableStateOf("")
    var snackbarMessage = mutableStateOf("")

    var tokenProvider: TokenProvider? = null
        set(value) {
            field = value
            value?.let { loadNotifications(it) }
        }

    private fun loadNotifications(tokenProvider: TokenProvider) {
        viewModelScope.launch {
            isLoading.value = true
            val service = JobService()
            val response = service.getMyJobsNotifications(tokenProvider)
            success = response.message == ""
            notifications.value = response.notificationModels
            isLoading.value = false
        }
    }

    fun confirmWorker(tokenProvider: TokenProvider, workerId: Int, jobId: Int, skillId: Int, workingStatusId: Int) {
        viewModelScope.launch {
            val service = JobService()
            val request = ConfirmWorkerRequest(workerId, jobId, skillId, workingStatusId)
            val response = service.confirmWorker(tokenProvider,request)
            success = response.success

            message.value = response.message ?: "Nema poruke"

            snackbarMessage.value = if (response.success) {
                message.value
            } else {
                "Došlo je do greške: ${response.message}"
            }
        }
    }

    fun getDistinctNotifications(): List<MyJobNotificationModel> {
        return notifications.value.distinctBy { it.jobId }
    }

    fun getNotificationsForJob(jobId: Int): List<MyJobNotificationModel> {
        return notifications.value.filter { it.jobId == jobId }
    }

    fun getNotificationsGroupedBySkill(jobId: Int): Map<String, List<MyJobNotificationModel>> {
        return getNotificationsForJob(jobId).groupBy { it.skill }
    }



    fun clearData() {
        success = false
        notifications.value = emptyList()
    }
}