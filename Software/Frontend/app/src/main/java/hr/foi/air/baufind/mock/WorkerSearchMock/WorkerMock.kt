import hr.foi.air.baufind.ws.model.Worker

object WorkerMock {

    val workers = listOf(
        Worker(
            id = 1,
            name = "Ivan Horvat",
            email = "ivan@email.com",
            phone = "099-123-4567",
            address = "Zagrebačka",
            numOfJobs = 5,
            skills = listOf("Vodoinstaler", "Keramičar"),
            availability = "Full-time",
            avgRating = 4.5
        ),
        Worker(
            id = 2,
            name = "Ana Kovač",
            email = "ana@email.com",
            phone = "098-765-4321",
            address = "Splitsko-dalmatinska",
            numOfJobs = 3,
            skills = listOf("Keramičar", "Električar"),
            availability = "Part-time",
            avgRating = 4.0
        ),
        Worker(
            id = 3,
            name = "Marko Novak",
            email = "marko@email.com",
            phone = "097-654-3210",
            address = "Međimurska",
            numOfJobs = 10,
            skills = listOf("Vodoinstaler", "Keramičar", "Instalater"),
            availability = "Freelance",
            avgRating = 4.8
        ),
        Worker(
            id = 4,
            name = "Luka Ivić",
            email = "luka@email.com",
            phone = "095-987-6543",
            address = "Zadarska",
            numOfJobs = 8,
            skills = listOf("Keramičar", "Majstor"),
            availability = "Full-time",
            avgRating = 4.7
        ),
        Worker(
            id = 5,
            name = "Kulen Ivić",
            email = "kulen@email.com",
            phone = "095-555-6543",
            address = "Ličko-senjska",
            numOfJobs = 7,
            skills = listOf("Majstor"),
            availability = "Full-time",
            avgRating = 4.6
        )
    )
}
