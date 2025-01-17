package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.request.CallForWorkingBody
import hr.foi.air.baufind.ws.request.ConfirmWorkerRequest
import hr.foi.air.baufind.ws.request.JobCreateBody
import hr.foi.air.baufind.ws.request.WorkerRequestJoinBody
import hr.foi.air.baufind.ws.response.CallForWorkingResponse
import hr.foi.air.baufind.ws.response.CheckJobNotificationResponse
import hr.foi.air.baufind.ws.response.ConfirmWorkerResponse
import hr.foi.air.baufind.ws.response.GetFullJobHistoryResponse
import hr.foi.air.baufind.ws.response.GetJobHistoryResponse
import hr.foi.air.baufind.ws.response.JobCreateResponse
import hr.foi.air.baufind.ws.response.JobResponse
import hr.foi.air.baufind.ws.response.JobsForCurrentUserResponse
import hr.foi.air.baufind.ws.response.MyJobNotificationResponse
import hr.foi.air.baufind.ws.response.SearchMyJobsForUserResponse
import hr.foi.air.baufind.ws.response.SearchPendingJobsForUserResponse
import hr.foi.air.baufind.ws.response.WorkerRequestJoinResponse
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

    @GET("/jobs/SearchPendingJobsForUser")
    suspend fun getPendingJobsForUser(): SearchPendingJobsForUserResponse

    @GET("/jobs/SearchMyJobsForUser")
    suspend fun getMyJobsForUser(): SearchMyJobsForUserResponse

    @POST("/jobs/requestJoin")
    suspend fun requestJoin(@Body workerRequestJoinBody: WorkerRequestJoinBody): WorkerRequestJoinResponse

    @GET("/jobs/CheckJobNotifications")
    suspend fun checkJobNotifications() : CheckJobNotificationResponse

    @GET("/jobs/getMyJobNotifications")
    suspend fun getMyJobNotifications(): MyJobNotificationResponse

    @PUT("/jobs/confirmWorker")
    suspend fun confirmWorker( @Body request: ConfirmWorkerRequest): ConfirmWorkerResponse

    @GET("/jobs/getHistory")
    suspend fun getFullJobHistory(): GetFullJobHistoryResponse

    @GET("/jobs/history/{id}")
    suspend fun getJobHistory(@Path("id") id: Int): GetJobHistoryResponse
}