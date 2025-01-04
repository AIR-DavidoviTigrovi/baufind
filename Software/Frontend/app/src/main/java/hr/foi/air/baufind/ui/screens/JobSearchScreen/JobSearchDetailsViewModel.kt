package hr.foi.air.baufind.ui.screens.JobSearchScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.baufind.service.JobGetService.JobGetResponse
import hr.foi.air.baufind.service.JobGetService.JobGetService
import hr.foi.air.baufind.ws.model.FullJobModel
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

class JobSearchDetailsViewModel : ViewModel() {
    var success : Boolean = false
    var message : String? = null
    var job : FullJobModel? = null
    var selectedJobId : Int = -1

    var tokenProvider: TokenProvider? = null
    set(value){
        field = value
        value?.let {loadJob(it)}
    }

    private fun loadJob(tokenProvider: TokenProvider){
        viewModelScope.launch {
            val service = JobGetService()
            Log.d("JobSearchDetailsViewModel", "Selected job id: $selectedJobId")
            val response : JobGetResponse = service.fetchJobAsync(selectedJobId, tokenProvider)
            success = response.success
            message = response.message
            job = response.job
        }
    }

    fun clearData(){
        success = false
        message = null
        job = null
    }

    fun isLoading() : Boolean{
        return job == null && message == null
    }

    fun HasError() : Boolean{
        return job == null && message != null
    }


}