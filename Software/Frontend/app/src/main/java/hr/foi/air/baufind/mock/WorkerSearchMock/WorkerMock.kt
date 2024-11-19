package hr.foi.air.baufind.mock.WorkerSearchMock

object WorkerMock {

    data class Worker(
        val id: Int,                
        val firstName: String,
        val lastName: String,
        val location: String,
        val numOfJobs: Int,
        val skills: List<String>,
        val availability: String,
        val expectedSalary: Double,
        val contactInfo: String?,
        val rating: Int
    )


    val workers = listOf(
        Worker(1, "Ivan", "Horvat", "Zagreb", 5, listOf("Vodoinstaler", "Keramičar"), "Full-time", 40000.0, "ivan@email.com",5),
        Worker(2, "Ana", "Kovač", "Split", 3, listOf("Keramičar", "Električar"), "Part-time", 30000.0, "ana@email.com",2),
        Worker(3, "Marko", "Novak", "Osijek", 10, listOf("Vodoinstaler", "Keramičar", "Instalater"), "Freelance", 45000.0, null,10),
        Worker(4, "Luka", "Ivić", "Zadar", 8, listOf("Keramičar", "Majstor"), "Full-time", 35000.0, "luka@email.com",7)
    )

}
