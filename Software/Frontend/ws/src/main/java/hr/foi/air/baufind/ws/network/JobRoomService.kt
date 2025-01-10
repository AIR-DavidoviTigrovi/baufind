package hr.foi.air.baufind.ws.network


import hr.foi.air.baufind.ws.response.JobRoomResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JobRoomService {
    @GET("/jobRoom/{jobID}")
    suspend fun getJobRoom(@Path("jobID") jobID: Int): JobRoomResponse
    @POST("/jobRoom/{jobID}")
    suspend fun getJobRoom(@Path("jobID") jobID: Int): JobRoomResponse
}