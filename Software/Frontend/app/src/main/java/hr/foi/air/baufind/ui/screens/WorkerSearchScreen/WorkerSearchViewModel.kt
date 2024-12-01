package hr.foi.air.baufind.ui.screens.WorkerSearchScreen

import WorkerMock
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hr.foi.air.baufind.service.WorkerService.WorkerSkillService
import hr.foi.air.baufind.ws.model.Worker
import hr.foi.air.baufind.ws.network.TokenProvider


class WorkerSearchViewModel() : ViewModel() {
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
     fun loadWorkers() {
         Log.e("loadWorkers", workers.value.toString())
         workers.value = WorkerMock.workers
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
}
