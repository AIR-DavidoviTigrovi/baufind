package hr.foi.air.baufind.service.SkillsService

import android.util.Log
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.response.GetAllSkillsResponse
import hr.foi.air.baufind.ws.response.SkillResponse

class SkillsService (private val tokenProvider: TokenProvider){
    val skillsNetworkService = NetworkService.createSkillsService(tokenProvider)
    suspend fun fetchAllSkills(): List<SkillResponse>?{
        return try {
            val response = skillsNetworkService.getAllSkills()
            if (!response.error.isNullOrEmpty()) {
                Log.e("SkillsFetch", "Error fetching skills: ${response.error}")
                null
            } else {
                response.skills
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }


        }
}