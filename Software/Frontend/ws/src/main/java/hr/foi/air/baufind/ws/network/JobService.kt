package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.request.JobCreateBody
import hr.foi.air.baufind.ws.response.JobCreateResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface JobService {
    @POST("/jobs/add")
    suspend fun addJob(@Body jobCreateBody: JobCreateBody): JobCreateResponse
}