package hr.foi.air.baufind.service.JobHistoryService

import android.util.Log
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider

class JobHistoryService {
    suspend fun fetchFullJobHistoryAsync(tokenProvider: TokenProvider): FullJobHistoryResponse {
        val service = NetworkService.createJobService(tokenProvider)
        try{
            val response = service.getFullJobHistory()
            Log.d("Puna povijest poslova je", response.toString())
            Log.d("Service svih poslova error:", response.error.toString())
            if(response.error == null){
                return FullJobHistoryResponse(
                    true,
                    "",
                    response.jobs
                )
            }else{
                return FullJobHistoryResponse(
                    false,
                    response.error,
                    listOf()
                )
            }
        }catch(e: Exception){
            e.printStackTrace()
            Log.d("Puna povijest poslova ERROR", "Error: ${e.message}")
            return FullJobHistoryResponse(
                false,
                "Pogreska pri fetchanju",
                listOf()
            )
        }
    }

    suspend fun fetchJobHistoryAsync(tokenProvider: TokenProvider): JobHistoryResponse {
        val service = NetworkService.createJobService(tokenProvider)
        try{
            val response = service.getJobHistory()
            Log.d("Povijest odabranog posla je", response.toString())
            if(response.error == ""){
                return JobHistoryResponse(
                    true,
                    "",
                    response.jobHistory
                )
            }else{
                return JobHistoryResponse(
                    false,
                    response.error,
                    null
                )
            }
        }catch(e: Exception){
            e.printStackTrace()
            Log.d("Povijest odabranog posla ERROR", "Error: ${e.message}")
            return JobHistoryResponse(
                false,
                "Pogreska pri fetchanju",
                null
            )
        }
    }
}