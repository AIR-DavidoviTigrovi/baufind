package hr.foi.air.baufind.service.JobService

data class JobDao(
    val name : String,
    val description : String,
    val allowInvitations : Boolean,
    val positions : List<Int>,
    val images : List<ByteArray>,
    val lat : Double,
    val long : Double,
    val location : String
)