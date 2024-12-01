package hr.foi.air.baufind.ws.network


import hr.foi.air.baufind.ws.response.WorkersSkillResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface WorkersSkillService {

    @GET("/workers/{skill}")
    suspend fun getWorkersBySkill(@Path("skill") skill: String): WorkersSkillResponse
}