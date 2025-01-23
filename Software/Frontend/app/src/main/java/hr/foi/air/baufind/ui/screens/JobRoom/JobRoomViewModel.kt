package hr.foi.air.baufind.ui.screens.JobRoom

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hr.foi.air.baufind.service.JobRoomService.JobRoomService
import hr.foi.air.baufind.service.JobRoomService.RoomOwnerState
import hr.foi.air.baufind.service.JobRoomService.RoomStatus
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
    val roomSkills: MutableState<List<Skill>> = mutableStateOf(emptyList())
    //Key je pozicija a Vrijednost su ljudi na njoj
    val peopleInRoom: MutableMap<String, MutableList<String>> = mutableMapOf<String,MutableList<String>>()
    val jobRoom: MutableState<List<JobRoom>> = mutableStateOf(emptyList())
    val mutableStatus : MutableState<String> = mutableStateOf("")
    val buttonStateText: MutableState<String> = mutableStateOf("")
    //----------------//

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
        mutableStatus.value = jobRoom.value[0].jobStatus

        if(jobRoom.value[0].jobStatus == "Zapocet"){
            buttonStateText.value = "Završi posao"
        }else if( jobRoom.value[0].jobStatus != "Zavrsen" && jobRoom.value[0].jobStatus != "Zapocet"){
            buttonStateText.value = "Započni posao"
        }else if(jobRoom.value[0].jobStatus == "Zavrsen"){
            buttonStateText.value = "Zavrsen"
        }
    }
    suspend fun loadJobPeople(jobId: Int) {
        val uniqueSkills = mutableSetOf<Skill>()
        for (room in jobRoom.value) {
            var work = peopleInRoom.getOrPut(room.skillTitle){
                mutableListOf()
            }.add(room.workerName)

            uniqueSkills.add(Skill(room.skillId, room.skillTitle))
        }
        roomSkills.value = uniqueSkills.toList()
    }
    suspend fun getAllSkills(){
        val service: SkillsService = SkillsService(tokenProvider = tokenProvider.value!!)
        listOfSkills.value = service.fetchAllSkills()!!

    }
    suspend fun setRoomStatus(jobId: Int,status: Int){
        var statusRoom = RoomStatus.listOfStatuses.keys.elementAt(status-1)
        val service = JobRoomService()
        val response = service.SetRoomStatus(jobId,status,tokenProvider.value!!)
        for (job in jobRoom.value){
            job.jobStatus = statusRoom
        }
        if(jobRoom.value[0].jobStatus == "Zapocet"){
            buttonStateText.value = "Završi posao"
        }else if( jobRoom.value[0].jobStatus != "Zavrsen" || jobRoom.value[0].jobStatus != "Zapocet"){
            buttonStateText.value = "Započni posao"
        }
    }
}