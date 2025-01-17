package hr.foi.air.baufind.ui.screens.JobHistoryScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.baufind.service.JobHistoryService.JobHistoryResponse
import hr.foi.air.baufind.service.JobHistoryService.JobHistoryService
import hr.foi.air.baufind.ws.model.JobHistoryModel
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

class SelectedJobHistoryViewModel : ViewModel() {
    var success: Boolean = false
    var message: String? = null
    val job = mutableStateOf<JobHistoryModel?>(null)
    var selectedJobId = mutableStateOf<Int?>(null)

    var tokenProvider: TokenProvider? = null
        set(value) {
            field = value
            value?.let { loadJobHistory(it) }
        }

    private fun loadJobHistory(tokenProvider: TokenProvider) {
        viewModelScope.launch {
            val service = JobHistoryService()
            val response: JobHistoryResponse = service.fetchJobHistoryAsync(tokenProvider, selectedJobId.value!!)
            Log.d("Povijest odabranog posla je", response.toString())
            success = response.success
            message = response.message
            job.value = response.jobHistory
        }
    }

    fun clearData(){
        success = false
        message = null
        job.value = null
    }

    fun isLoading() : Boolean{
        return job.value == null && message == null
    }

    fun hasError() : Boolean{
        return job.value == null && message != null
    }
}