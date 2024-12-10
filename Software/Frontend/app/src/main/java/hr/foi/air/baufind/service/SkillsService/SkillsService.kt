package hr.foi.air.baufind.service.SkillsService

import android.util.Log
import androidx.compose.runtime.MutableState
import hr.foi.air.baufind.ws.model.Skill
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider

class SkillsService(tokenProvider: TokenProvider){
    private val skillsNetworkService = NetworkService.createSkillService(tokenProvider)
    suspend fun fetchAllSkills(): List<Skill>?{
        return try {
            val response = skillsNetworkService.getAllSkills()
            if (response.error.isNotEmpty()) {
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