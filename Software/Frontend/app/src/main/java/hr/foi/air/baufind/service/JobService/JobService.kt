package hr.foi.air.baufind.service.JobService

import android.util.Log
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.request.JobCreateBody
import java.util.Base64

class JobService(){
    suspend fun addJobAsync(jobDao: JobDao, tokenProvider: TokenProvider): JobResponse {
        val service = NetworkService.createJobService(tokenProvider)

        val jobBody = JobCreateBody(
            title = jobDao.name,
            description = jobDao.description,
            allow_worker_invite = jobDao.allowInvitations,
            skills = jobDao.positions,
            pictures = jobDao.images.map{ Base64.getEncoder().encodeToString(it)},
            latitude = jobDao.lat,
            longitude = jobDao.long,
            location = jobDao.location
        )

        Log.d("jobBody", jobBody.toString())

        try{
            val response = service.addJob(jobBody)
            if(response.success != "") {
                return JobResponse(
                    true,
                    response.success
                )
            }
            else {
                return JobResponse(
                    false,
                    response.error
                )
            }
        }catch(e: Exception){
            e.printStackTrace()
            return JobResponse(
                false,
                "Pogreska pri fetchanju"
            )
        }
    }

    suspend fun getPendingJobsForUser(tokenProvider: TokenProvider): PendingJobsForUserResponse {
        val service = NetworkService.createJobService(tokenProvider)

        try {
            val response = service.getPendingJobsForUser()
            if (!response.success) {
                return PendingJobsForUserResponse(
                    false,
                    response.error
                )
            }

            return PendingJobsForUserResponse(
                true,
                null,
                response.jobs
            )
        } catch (e: Exception) {
            return PendingJobsForUserResponse(
                false,
                e.message
            )
        }
    }
}