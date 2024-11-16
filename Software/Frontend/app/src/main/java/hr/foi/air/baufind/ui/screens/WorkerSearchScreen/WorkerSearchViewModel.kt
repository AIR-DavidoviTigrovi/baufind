package hr.foi.air.baufind.ui.screens.WorkerSearchScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.foi.air.baufind.mock.WorkerSearchMock.WorkerMock
class WorkerSearchViewModel : ViewModel() {
    val workers: MutableLiveData<List<WorkerMock.Worker>> = MutableLiveData(WorkerMock.workers)
}