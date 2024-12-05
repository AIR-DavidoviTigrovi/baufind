import hr.foi.air.baufind.ws.model.Worker

object WorkerMock {

    val workers = listOf(
        Worker(
            id = 1,
            name = "Ivan Horvat",
            address = "Zagrebačka",
            numOfJobs = 5,
            skills = "Vodoinstaler",
            avgRating = 4.5
        ),
        Worker(
            id = 2,
            name = "Ana Kovač",
            address = "Splitsko-dalmatinska",
            numOfJobs = 3,
            skills = "Keramičar",
            avgRating = 4.0
        ),
        Worker(
            id = 3,
            name = "Marko Novak",
            address = "Međimurska",
            numOfJobs = 10,
            skills =  "Keramičar",
            avgRating = 4.8
        ),
        Worker(
            id = 4,
            name = "Luka Ivić",
            address = "Zadarska",
            numOfJobs = 8,
            skills = "Keramičar",
            avgRating = 4.7
        ),
        Worker(
            id = 5,
            name = "Kulen Ivić",
            address = "Ličko-senjska",
            numOfJobs = 7,
            skills = "Majstor",
            avgRating = 4.6
        )
    )
}
