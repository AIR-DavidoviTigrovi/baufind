package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.model.Worker
import hr.foi.air.baufind.ws.request.WorkersSkillBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface WorkersSkillService {

    @GET("workers/skills")
    suspend fun getWorkersBySkill(@Body workersSkillBody: WorkersSkillBody): List<Worker>
}