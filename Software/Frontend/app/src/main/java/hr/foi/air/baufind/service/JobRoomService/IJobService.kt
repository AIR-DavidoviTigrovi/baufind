package hr.foi.air.baufind.service.JobRoomService

import hr.foi.air.baufind.ws.model.JobRoom
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.response.JobRoomResponse

interface IJobService {
    suspend fun GetRoomForJob(jobID:Int, tokenProvider: TokenProvider): List<JobRoom>;
}