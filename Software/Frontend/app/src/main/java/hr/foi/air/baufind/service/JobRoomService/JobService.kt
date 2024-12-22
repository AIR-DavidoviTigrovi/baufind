package hr.foi.air.baufind.service.JobRoomService

import hr.foi.air.baufind.ws.model.JobRoom
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.response.JobRoomResponse

class JobService : IJobService {
    override suspend fun GetRoomForJob(jobID: Int,tokenProvider: TokenProvider): List<JobRoom> {
        val service = NetworkService.createJobRoomService(tokenProvider)
        try {
            val response = service.getJobRoom(jobID)
            return response.jobRooms
        }catch (err: Exception) {
            err.printStackTrace()
            return emptyList()
        }
    }

}