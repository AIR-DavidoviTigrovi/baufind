package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.request.WorkersSkillBody
import hr.foi.air.baufind.ws.response.WorkersSkillResponse
import retrofit2.http.Body
import retrofit2.http.GET

interface WorkersSkillService {

    @GET("workers/skills")
    suspend fun getWorkersBySkill(@Body workersSkillBody: WorkersSkillBody): WorkersSkillResponse
}