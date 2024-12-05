package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.response.GetAllSkillsResponse
import retrofit2.http.GET

interface SkillsService {
    @GET("skills")
    suspend fun getAllSkills(): GetAllSkillsResponse
}