package hr.foi.air.baufind.ws.network


import hr.foi.air.baufind.ws.response.WorkersSkillResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface WorkersSkillService {

    @GET("/workers/{skill}/{ids}")
    suspend fun getWorkersBySkill(@Path("skill") skill: String,@Path ("ids") ids: String): WorkersSkillResponse
}