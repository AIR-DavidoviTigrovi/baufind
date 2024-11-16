package hr.foi.air.baufind.mock.WorkerSearchMock

object WorkerMock {

    data class Worker(
        val id: Int,                // Jedinstveni identifikator radnika
        val firstName: String,      // Ime radnika
        val lastName: String,       // Prezime radnika
        val location: String,       // Lokacija radnika (grad, regija)
        val numOfJobs: Int,   // Broj poslova
        val skills: List<String>,   // Lista vještina radnika (vodoinstaler, keramičar)
        val availability: String,   // Dostupnost (npr. puno radno vrijeme, honorarni rad)
        val expectedSalary: Double, // Očekivana plaća radnika
        val contactInfo: String?    // Kontakt podaci (ako postoji)
    )


    val workers = listOf(
        Worker(1, "Ivan", "Horvat", "Zagreb", 5, listOf("Vodoinstaler", "Keramičar"), "Full-time", 40000.0, "ivan@email.com"),
        Worker(2, "Ana", "Kovač", "Split", 3, listOf("Keramičar", "Električar"), "Part-time", 30000.0, "ana@email.com"),
        Worker(3, "Marko", "Novak", "Osijek", 10, listOf("Vodoinstaler", "Keramičar", "Instalater"), "Freelance", 45000.0, null),
        Worker(4, "Luka", "Ivić", "Zadar", 8, listOf("Keramičar", "Majstor"), "Full-time", 35000.0, "luka@email.com")
    )

}
