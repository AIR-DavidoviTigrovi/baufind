package hr.foi.air.baufind.ui.screens.JobRoom

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hr.foi.air.baufind.service.JobRoomService.JobRoomService
import hr.foi.air.baufind.ws.model.JobRoom
import hr.foi.air.baufind.ws.network.TokenProvider

class JobRoomViewModel: ViewModel() {
    val tokenProvider: MutableState<TokenProvider?> = mutableStateOf(null)
    val service = JobRoomService()
    val jobRoom: MutableState<List<JobRoom>> = mutableStateOf(emptyList())
    suspend fun getJobRoom(jobId: Int) {
        var response = service.GetRoomForJob(jobId,tokenProvider.value!!)
        jobRoom.value = response
    }
}