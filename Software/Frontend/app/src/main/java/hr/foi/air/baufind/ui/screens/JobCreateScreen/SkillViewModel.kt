package hr.foi.air.baufind.ui.screens.JobCreateScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.baufind.service.SkillsService.SkillsService
import hr.foi.air.baufind.ws.model.Skill
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

class SkillViewModel : ViewModel() {
    val skill : MutableState<List<Skill>> = mutableStateOf(emptyList())
    var tokenProvider: TokenProvider? = null
        set(value) {
            field = value
            value?.let { loadSkills(it) }
        }
    private fun loadSkills(tokenProvider: TokenProvider) {
        viewModelScope.launch {
            val service = SkillsService(tokenProvider)
            skill.value = service.fetchAllSkills().orEmpty()
        }
    }
}