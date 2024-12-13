package hr.foi.air.baufind.service.JobSearchService

import android.util.Log
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider

class JobSearchService() {
    suspend fun fetchJobsForCurrentUserAsync(tokenProvider: TokenProvider): JobSearchResponse {
        val service = NetworkService.createJobService(tokenProvider)
        try {
            val response = service.getJobsForCurrentUser()
            Log.d("JobSearchService response", response.toString())
            if (response.error == "") {
                return JobSearchResponse(
                    true,
                    "",
                    response.jobs
                )
            } else {
                return JobSearchResponse(
                    false,
                    response.error,
                    listOf()
                )
            }
        }catch(e: Exception){
            e.printStackTrace()
            Log.d("JobSearchService", "Error: ${e.message}")
            return JobSearchResponse(
                false,
                "Pogreska pri fetchanju",
                listOf()
            )
        }
    }
}