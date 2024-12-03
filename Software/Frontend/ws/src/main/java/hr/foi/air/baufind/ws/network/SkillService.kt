package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.response.SkillResponse
import retrofit2.http.GET

interface SkillService {

    @GET("/skills")
    suspend fun getAllSkills(): SkillResponse
}