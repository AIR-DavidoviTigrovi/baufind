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

    val workers: MutableLiveData<List<WorkerMock.Worker>> = MutableLiveData(WorkerMock.workers)
}
