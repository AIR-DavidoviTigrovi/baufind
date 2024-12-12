package hr.foi.air.baufind.service.JobSearchService

import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider

class JobSearchService() {
    suspend fun fetchJobsForCurrentUserAsync(tokenProvider: TokenProvider): JobSearchResponse {
        val service = NetworkService.createJobService(tokenProvider)
        try {
            val response = service.getJobsForCurrentUser()
            if (response.Error == null) {
                return JobSearchResponse(
                    true,
                    "",
                    response.Jobs
                )
            } else {
                return JobSearchResponse(
                    false,
                    response.Error,
                    listOf()
                )
            }
        }catch(e: Exception){
            e.printStackTrace()
            return JobSearchResponse(
                false,
                "Pogreska pri fetchanju",
                listOf()
            )
        }
    }
}