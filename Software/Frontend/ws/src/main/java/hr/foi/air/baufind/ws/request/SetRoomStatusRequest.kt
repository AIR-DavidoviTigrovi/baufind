package hr.foi.air.baufind.ws.request

data class SetRoomStatusRequest(
    val jobId: Int,
    val status: Int
)
