package hr.foi.air.baufind.ui.screens.PendingJobsScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.baufind.service.JobService.JobService
import hr.foi.air.baufind.ws.model.JobWorkingModel
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

class PendingJobsViewModel : ViewModel() {
    var success : Boolean = false
    var isLoading = mutableStateOf(false)
    val jobs = mutableStateOf(emptyList<JobWorkingModel>())

    var tokenProvider: TokenProvider? = null
    set(value) {
        field = value
        value?.let { loadJobs(it) }
    }

    private fun loadJobs(tokenProvider: TokenProvider) {
        viewModelScope.launch {
            isLoading.value = true
            val service = JobService()
            val response = service.getPendingJobsForUser(tokenProvider)
            success = response.success
            jobs.value = response.jobs
            isLoading.value = false
            Log.i("PendingJobsViewModel", jobs.value.toString())
        }
    }

    fun clearData() {
        success = false
        jobs.value = emptyList()
    }
}