package hr.foi.air.baufind.ui.screens.JobHistoryScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.baufind.service.JobHistoryService.FullJobHistoryResponse
import hr.foi.air.baufind.service.JobHistoryService.JobHistoryService
import hr.foi.air.baufind.ws.model.AllJobsHistoryModel
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

class WholeHistoryViewModel : ViewModel() {
    var success : Boolean = false
    var message : String? = null
    val jobs = mutableStateOf(emptyList<AllJobsHistoryModel>())

    var tokenProvider: TokenProvider? = null
    set(value){
        field = value
        value?.let { loadHistory(it) }
    }

    private fun loadHistory(tokenProvider: TokenProvider){
        viewModelScope.launch {
            val service = JobHistoryService()
            val response : FullJobHistoryResponse = service.fetchFullJobHistoryAsync(tokenProvider)
            Log.d("loadHistory je dobio", response.toString())
            success = response.success
            message = response.message
            jobs.value = response.jobs
        }
    }

    fun clearData(){
        success = false
        message = null
        jobs.value = emptyList()
    }

    fun isLoading() : Boolean{
        return jobs.value.isEmpty() && message == null
    }

    fun hasError() : Boolean{
        return jobs.value.isEmpty() && message != null
    }

}