package hr.foi.air.baufind.service.JobGetService

import android.util.Log
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider

class JobGetService() {
    suspend fun fetchJobAsync(id: Int, tokenProvider: TokenProvider): JobGetResponse {
        val service = NetworkService.createJobService(tokenProvider)
        try {
            val response = service.getJob(id)
            if (response.error == "") {
                return JobGetResponse(
                    true,
                    "",
                    response.job
                )
            } else {
                return JobGetResponse(
                    false,
                    response.error,
                    null
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return JobGetResponse(
                false,
                "Pogreska pri fetchanju",
                null
            )
        }
    }
}