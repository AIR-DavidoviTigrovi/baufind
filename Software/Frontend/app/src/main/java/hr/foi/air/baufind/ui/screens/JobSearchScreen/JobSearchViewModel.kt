package hr.foi.air.baufind.ui.screens.JobSearchScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.baufind.service.JobSearchService.JobSearchResponse
import hr.foi.air.baufind.service.JobSearchService.JobSearchService
import hr.foi.air.baufind.ws.model.JobSearchModel
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

class JobSearchViewModel : ViewModel(){
    var success : Boolean = false
    var message : String? = null
    val jobs = mutableStateOf(emptyList<JobSearchModel>())
    var selectedJobId : Int = 0

    var tokenProvider: TokenProvider? = null
    set(value) {
        field = value
        value?.let { loadJobs(it) }
    }

    private fun loadJobs(tokenProvider: TokenProvider) {
        viewModelScope.launch {
            val service = JobSearchService()
            val response : JobSearchResponse = service.fetchJobsForCurrentUserAsync(tokenProvider)
            Log.d("JobSearchViewModel", "Response: $response")
            success = response.success
            message = response.message
            jobs.value = response.jobs
        }
    }

    fun clearData(){
        selectedJobId = 0
    }

    fun isLoading() : Boolean{
        return jobs.value.isEmpty() && message == null
    }

    fun hasError() : Boolean{
        return jobs.value.isEmpty() && message != null
    }
}