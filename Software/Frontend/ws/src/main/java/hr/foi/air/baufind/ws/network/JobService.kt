package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.request.CallForWorkingBody
import hr.foi.air.baufind.ws.request.JobCreateBody
import hr.foi.air.baufind.ws.response.CallForWorkingResponse
import hr.foi.air.baufind.ws.response.JobCreateResponse
import hr.foi.air.baufind.ws.response.JobResponse
import hr.foi.air.baufind.ws.response.JobsForCurrentUserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JobService {
    @POST("/jobs/add")
    suspend fun addJob(@Body jobCreateBody: JobCreateBody): JobCreateResponse

    @GET("/jobs/search")
    suspend fun getJobsForCurrentUser(): JobsForCurrentUserResponse

    @GET("/jobs/{id}")
    suspend fun getJob(@Path("id") id: Int): JobResponse

    @PUT("/jobs/CallForWorking")
    suspend fun callForWorking(@Body request: CallForWorkingBody): CallForWorkingResponse

}