package hr.foi.air.baufind.ui.screens.MyJobsScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.baufind.service.JobService.JobService
import hr.foi.air.baufind.ws.model.JobWorkingModel
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

class MyJobsViewModel : ViewModel() {
    var success : Boolean = false
    val jobs = mutableStateOf(emptyList<JobWorkingModel>())

    var tokenProvider: TokenProvider? = null
    set(value) {
        field = value
        value?.let { loadJobs(it) }
    }

    private fun loadJobs(tokenProvider: TokenProvider) {
        viewModelScope.launch {
            val service = JobService()
            val response = service.getMyJobsForUser(tokenProvider)
            success = response.success
            jobs.value = response.jobs
        }
    }

    fun clearData() {
        success = false
        jobs.value = emptyList()
    }
}