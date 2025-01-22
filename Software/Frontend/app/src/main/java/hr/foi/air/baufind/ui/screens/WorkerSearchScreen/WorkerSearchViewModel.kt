package hr.foi.air.baufind.ui.screens.WorkerSearchScreen

import WorkerMock
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.baufind.service.SkillsService.SkillsService
import hr.foi.air.baufind.service.WorkerService.CallForWorkingRequest
import hr.foi.air.baufind.service.WorkerService.CallForWorkingResponse
import hr.foi.air.baufind.service.WorkerService.IWorkerSkillService
import hr.foi.air.baufind.service.WorkerService.WorkerSkillService
import hr.foi.air.baufind.ui.components.Skill
import hr.foi.air.baufind.ws.model.Worker
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.request.WorkersSkillBody
import kotlinx.coroutines.launch


class WorkerSearchViewModel() : ViewModel() {
    val skillsId: MutableState<MutableList<Int>> = mutableStateOf(mutableListOf())
    var skill : MutableState<MutableList<Skill>> = mutableStateOf(mutableListOf())
    var isEmptyList :  Boolean = false
    val listofIDs : MutableState<MutableList<Int>> = mutableStateOf(mutableListOf())
    var skillStrings : MutableState<List<String>> = mutableStateOf(emptyList())
    val tokenProvider: MutableState<TokenProvider?> = mutableStateOf(null)
    val isExpandedL: MutableState<Boolean> = mutableStateOf(false)
    val isExpandedR: MutableState<Boolean> = mutableStateOf(false)
    val selectedItemL: MutableState<String> = mutableStateOf("")
    val selectedItemR: MutableState<String> = mutableStateOf("")
    val optionsR: List<String> = listOf("Ocjena ASC", "Ocjena DESC", "Broj poslova ASC", "Broj poslova DESC")
    val optionsL: List<String> = listOf("Zagrebačka", "Krapinsko-zagorska", "Sisačko-moslavačka", "Karlovačka", "Varaždinska",
        "Bjelovarsko-bilogorska", "Primorsko-goranska", "Ličko-senjska",
        "Virovitičko-podravska", "Osječko-baranjska", "Šibensko-kninska", "Vukovarsko-srijemska",
        "Zadarska", "Međimurska", "Dubrovničko-neretvanska", "Istarska", "Požeško-slavonska",
        "Splitsko-dalmatinska", "Grad Zagreb", "Splitsko-dalmatinska",
    )
    val service = WorkerSkillService()
    val workers: MutableState<List<Worker>> = mutableStateOf(emptyList())
    val filteredWorkers: MutableState<List<Worker>> = mutableStateOf(emptyList())
    private val workersService : IWorkerSkillService = WorkerSkillService()


    suspend fun getAllSkills (){
        val skillService = SkillsService(tokenProvider.value!!)
        var skillToFilter = skillService.fetchAllSkills().orEmpty()
        Log.e("Prije fetchanja: ", skillsId.value.toString())
        if (skillToFilter.isNotEmpty()) {
            skill.value =mutableListOf()
            skillStrings.value = emptyList()
            for (i in skillToFilter){
                if(skillsId.value.contains(i.id)){
                    skill.value += Skill(i.id, i.title)
                    skillStrings.value += i.title
                }
            }
        }
        Log.e("Nakon fetchanja: ", skillsId.value.toString())


    }
    //Funkcija za filtriranje radnika
    fun updateFilteredWorkersL(option: String) {
        selectedItemL.value = option
        filteredWorkers.value = emptyList()
        Log.e("filteredWorkers", filteredWorkers.value.toString())
        workers.value.forEach {
            if (it.address == option){
                filteredWorkers.value += it
                Log.e("filteredWorkers", filteredWorkers.value.toString())
            }
        }
        isExpandedL.value = false
    }
    //Funkcija za učitavanje radnika
     fun loadWorkersMock() {
         viewModelScope.launch {
         Log.e("loadWorkers", workers.value.toString())
         workers.value = WorkerMock.workers
         filteredWorkers.value = workers.value
        }
    }
    suspend fun loadWorkers() {
        workers.value =  service.getWorkersBySkill(listofIDs.value.toString(),workersSkillBody = WorkersSkillBody(skillStrings.value.toString()),
            tokenProvider = tokenProvider.value!!)
        Log.e("getWorkersBySkill", skill.value.toString())
        filteredWorkers.value = workers.value
    }
    //Funkcija za filtriranje radnika
    fun updateFilteredWorkersR(option: String) {
        selectedItemR.value = option
        workers.value.forEach {
            if(option == "Ocjena ASC"){
                filteredWorkers.value = filteredWorkers.value.sortedBy { it.avgRating }

            }
            if(option == "Ocjena DESC") {
                filteredWorkers.value =
                   filteredWorkers.value.sortedByDescending { it.avgRating }
            }
            if(option == "Broj poslova ASC"){
                filteredWorkers.value = filteredWorkers.value.sortedBy { it.numOfJobs }
            }
            if(option == "Broj poslova DESC"){
                filteredWorkers.value = filteredWorkers.value.sortedByDescending { it.numOfJobs }
            }
        }
        isExpandedR.value = false
    }

    suspend fun callWorkerToJob(jobId: Int, skillId: Int, workerId: Int): CallForWorkingResponse {
        try {
            val success = workersService.callWorkerToJob(
                CallForWorkingRequest( workerId,  jobId, skillId),
                tokenProvider = tokenProvider.value!!
            )
            return success
        } catch (e: Exception) {
            e.printStackTrace()
            return CallForWorkingResponse(
                message = "Greška prilikom fetchanja",
                success = false
            )
        }
    }

    fun clearData(){
        skill.value =mutableListOf()
        skillStrings.value = emptyList()
        isEmptyList = false
        skillsId.value = mutableListOf()
        filteredWorkers.value = emptyList()
        workers.value = emptyList()
    }

}
