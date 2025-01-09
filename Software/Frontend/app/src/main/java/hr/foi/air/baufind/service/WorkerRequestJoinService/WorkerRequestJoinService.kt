package hr.foi.air.baufind.service.WorkerRequestJoinService

import android.util.Log
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.request.WorkerRequestJoinBody

class WorkerRequestJoinService {
    suspend fun sendJoinRequestAsync(skillId: Int, jobId: Int, tokenProvider: TokenProvider): WorkerRequestJoinResponse{
        val service = NetworkService.createJobService(tokenProvider)
        val body = WorkerRequestJoinBody(
            skillId = skillId,
            jobId = jobId
        )
        try{
            val response = service.requestJoin(body)
            if(response.success){
                return WorkerRequestJoinResponse(
                    true,
                    response.message
                )
            }else{
                return WorkerRequestJoinResponse(
                    false,
                    response.message
                )
            }
        }catch(e: Exception){
            e.printStackTrace()
            Log.d("WorkerRequestJoinService", "Error: ${e.message}")
            return WorkerRequestJoinResponse(
                false,
                "Pogreska pri slanju",
            )
        }
    }
}