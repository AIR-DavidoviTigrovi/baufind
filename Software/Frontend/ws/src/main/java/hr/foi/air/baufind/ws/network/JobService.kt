package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.request.JobCreateBody
import hr.foi.air.baufind.ws.request.WorkerRequestJoinBody
import hr.foi.air.baufind.ws.response.JobCreateResponse
import hr.foi.air.baufind.ws.response.JobResponse
import hr.foi.air.baufind.ws.response.JobsForCurrentUserResponse
import hr.foi.air.baufind.ws.response.SearchMyJobsForUserResponse
import hr.foi.air.baufind.ws.response.SearchPendingJobsForUserResponse
import hr.foi.air.baufind.ws.response.WorkerRequestJoinResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JobService {
    @POST("/jobs/add")
    suspend fun addJob(@Body jobCreateBody: JobCreateBody): JobCreateResponse

    @GET("/jobs/search")
    suspend fun getJobsForCurrentUser(): JobsForCurrentUserResponse

    @GET("/jobs/{id}")
    suspend fun getJob(@Path("id") id: Int): JobResponse

    @GET("/jobs/SearchPendingJobsForUser")
    suspend fun getPendingJobsForUser(): SearchPendingJobsForUserResponse

    @GET("/jobs/SearchMyJobsForUser")
    suspend fun getMyJobsForUser(): SearchMyJobsForUserResponse

    @POST("/jobs/requestJoin")
    suspend fun requestJoin(@Body workerRequestJoinBody: WorkerRequestJoinBody): WorkerRequestJoinResponse
}