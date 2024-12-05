package hr.foi.air.baufind.service.SkillService

import android.util.Log
import hr.foi.air.baufind.ws.model.Skill
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider

class SkillService : ISkillService{
    override suspend fun GetAllSkills(tokenProvider: TokenProvider): List<Skill> {
        val service = NetworkService.createSkillService(tokenProvider)
        try{
            val response = service.getAllSkills()
            return response.skills
        }catch(err: Exception){
            err.printStackTrace()
            return emptyList()
        }
    }
}