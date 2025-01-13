package hr.foi.air.baufind.service.JobRoomService

import hr.foi.air.baufind.ws.model.JobRoom
import hr.foi.air.baufind.ws.model.SetRoomStatus
import hr.foi.air.baufind.ws.network.TokenProvider

interface IJobRoomService {
    suspend fun GetRoomForJob(jobID:Int, tokenProvider: TokenProvider): List<JobRoom>;
    suspend fun SetRoomStatus(jobId: Int,status: Int,tokenProvider: TokenProvider): SetRoomStatus

}