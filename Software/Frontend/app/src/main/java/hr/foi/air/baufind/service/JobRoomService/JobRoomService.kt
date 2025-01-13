package hr.foi.air.baufind.service.JobRoomService

import hr.foi.air.baufind.ws.model.JobRoom
import hr.foi.air.baufind.ws.model.SetRoomStatus
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.request.SetRoomStatusRequest

class JobRoomService : IJobRoomService {
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

    override suspend fun SetRoomStatus(jobId: Int, status: Int, tokenProvider: TokenProvider): SetRoomStatus {
        val service = NetworkService.createJobRoomService(tokenProvider)
        try {
            val response = service.setRoomStatus(SetRoomStatusRequest(jobId,status))
            return SetRoomStatus("Uspjeh",null)
        }catch (err: Exception) {
            err.printStackTrace()
            return SetRoomStatus(null,err.message)
        }
    }

}