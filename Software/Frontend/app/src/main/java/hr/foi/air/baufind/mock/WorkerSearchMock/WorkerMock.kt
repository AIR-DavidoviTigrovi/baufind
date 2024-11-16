package hr.foi.air.baufind.mock.WorkerSearchMock

object WorkerMock {

    data class Worker(
        val id: Int,                // Jedinstveni identifikator radnika
        val firstName: String,      // Ime radnika
        val lastName: String,       // Prezime radnika
        val location: String,       // Lokacija radnika (grad, regija)
        val experienceYears: Int,   // Broj godina radnog iskustva
        val skills: List<String>,   // Lista vještina radnika (npr. Java, Python, Vođenje projekata)
        val jobCategory: String,    // Kategorija posla (npr. IT, Marketing, Administracija)
        val availability: String,   // Dostupnost (npr. puno radno vrijeme, honorarni rad)
        val expectedSalary: Double, // Očekivana plata radnika
        val resumeLink: String?,    // Link na životopis (ako postoji)
        val contactInfo: String?    // Kontakt podaci (ako postoji)
    )


    val workers = listOf(
        Worker(1, "Ivan", "Horvat", "Zagreb", 3, listOf("Java", "Spring", "SQL"), "IT", "Full-time", 50000.0, "https://linktoresume.com/ivan", "ivan@email.com"),
        Worker(2, "Ana", "Kovač", "Split", 5, listOf("Marketing", "SEO", "Content Creation"), "Marketing", "Freelance", 35000.0, null, "ana@email.com"),
        Worker(3, "Marko", "Novak", "Osijek", 7, listOf("JavaScript", "Node.js", "React"), "IT", "Part-time", 45000.0, null, "marko@email.com"),
        Worker(4, "Luka", "Ivić", "Zagreb", 2, listOf("Excel", "Data Analysis", "SQL"), "Administration", "Full-time", 40000.0, null, "luka@email.com")
    )
}
