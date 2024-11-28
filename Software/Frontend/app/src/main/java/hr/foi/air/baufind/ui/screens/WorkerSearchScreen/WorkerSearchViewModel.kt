package hr.foi.air.baufind.ui.screens.WorkerSearchScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.foi.air.baufind.mock.WorkerSearchMock.WorkerMock

class WorkerSearchViewModel : ViewModel() {

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

    val workers: MutableState<List<WorkerMock.Worker>> = mutableStateOf(WorkerMock.workers)
    val filteredWorkers: MutableState<List<WorkerMock.Worker>> = mutableStateOf(workers.value)
    fun updateFilteredWorkersL(option: String) {
        selectedItemL.value = option
        filteredWorkers.value = emptyList()
        workers.value.forEach {
            if (it.location == option){
                filteredWorkers.value += it
            }
        }
        isExpandedL.value = false
    }
    fun updateFilteredWorkersR(option: String) {
        selectedItemR.value = option
        workers.value.forEach {
            if(option == "Ocjena ASC"){
                filteredWorkers.value = filteredWorkers.value.sortedBy { it.rating }

            }
            if(option == "Ocjena DESC") {
                filteredWorkers.value =
                   filteredWorkers.value.sortedByDescending { it.rating }
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
