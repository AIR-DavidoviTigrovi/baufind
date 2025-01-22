package hr.foi.air.baufind.service.JobRoomService

object RoomStatus {
    val listOfStatuses: Map<String, Int> = mapOf(
        "Nema radnika" to 1,
        "Čeka odobrenje poslodavca" to 2,
        "Završen" to 3,
        "Započet" to 4
    )
}