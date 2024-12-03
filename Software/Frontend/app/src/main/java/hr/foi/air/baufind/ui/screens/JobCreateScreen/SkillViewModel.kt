package hr.foi.air.baufind.ui.screens.JobCreateScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hr.foi.air.baufind.service.SkillService.SkillService
import hr.foi.air.baufind.ws.model.Skill
import hr.foi.air.baufind.ws.network.TokenProvider

class SkillViewModel : ViewModel() {
    val skill : MutableState<List<Skill>> = mutableStateOf(emptyList())
    val tokenProvider: MutableState<TokenProvider?> = mutableStateOf(null)

    val service = SkillService()

    suspend fun loadSkills(){
        skill.value = service.GetAllSkills(tokenProvider = tokenProvider.value!!)
    }
}