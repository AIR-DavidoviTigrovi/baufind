package hr.foi.air.baufind.ws.request

data class JobCreateBody(
    val title : String,
    val description : String,
    val allow_worker_invite : Boolean,
    val location : String,
    val skills : List<Int>,
    val pictures : List<String>,
    val latitude : Double,
    val longitude : Double
)
