package hr.foi.air.baufind.service.JobRoomService

object RoomStatus {
    val listOfStatuses: Map<String, Int> = mapOf(
        "Nema radnika" to 1,
        "Čeka odobrenje poslodavca" to 2,
        "Zavrsen" to 3,
        "Zapocet" to 4
    )
}