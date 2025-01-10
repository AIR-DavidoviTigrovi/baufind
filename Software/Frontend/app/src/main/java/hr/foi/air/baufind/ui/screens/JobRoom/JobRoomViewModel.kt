package hr.foi.air.baufind.ui.screens.JobRoom

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hr.foi.air.baufind.service.JobRoomService.JobRoomService
import hr.foi.air.baufind.service.JobRoomService.RoomOwnerState
import hr.foi.air.baufind.service.SkillsService.SkillsService
import hr.foi.air.baufind.service.jwtService.JwtService
import hr.foi.air.baufind.ws.model.JobRoom
import hr.foi.air.baufind.ws.model.Skill
import hr.foi.air.baufind.ws.network.TokenProvider

class JobRoomViewModel: ViewModel() {
    val tokenProvider: MutableState<TokenProvider?> = mutableStateOf(null)
    val service = JobRoomService()
    val roomOwnerState: MutableState<RoomOwnerState?> = mutableStateOf(null)
    val listOfSkills: MutableState<List<Skill>> = mutableStateOf(emptyList())
    //Key je ime korisnika a value je uloga radnika
    val peopleInRoom: MutableMap<String, String> = mutableMapOf<String,String>()
    val jobRoom: MutableState<List<JobRoom>> = mutableStateOf(emptyList())
    suspend fun determinateOwner(context: Context){
        val jwt = JwtService.getJwt(context = context)
        val userId = JwtService.getIdFromJwt(jwt!!)

        if(jobRoom.value[0].employerId == userId!!.toInt()){
            roomOwnerState.value = RoomOwnerState.Employer
        }else{
            roomOwnerState.value = RoomOwnerState.Worker
        }

    }
    suspend fun getJobRoom(jobId: Int) {
        var response = service.GetRoomForJob(jobId,tokenProvider.value!!)
        jobRoom.value = response

    }
    suspend fun loadJobPeople(jobId: Int) {
        for(room in jobRoom.value){
            peopleInRoom[room.workerName] = room.skillTitle
        }
    }
    suspend fun getAllSkills(){
        val service: SkillsService = SkillsService(tokenProvider = tokenProvider.value!!)
        listOfSkills.value = service.fetchAllSkills()!!

    }
}