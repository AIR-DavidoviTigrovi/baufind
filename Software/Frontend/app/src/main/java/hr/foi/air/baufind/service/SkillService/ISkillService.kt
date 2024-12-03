package hr.foi.air.baufind.service.SkillService

import hr.foi.air.baufind.ws.model.Skill
import hr.foi.air.baufind.ws.network.TokenProvider

interface ISkillService {
    suspend fun GetAllSkills(tokenProvider: TokenProvider) : List<Skill>
}