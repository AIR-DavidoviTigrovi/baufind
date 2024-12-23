package hr.foi.air.baufind.ws.response

import hr.foi.air.baufind.ws.model.JobRoom

data class JobRoomResponse(
    val jobRooms: List<JobRoom>,
    val error: String?
)
