package hr.foi.air.baufind.ws.model

import java.time.format.DateTimeFormatter


data class User(
    val id : Int,
    val name : String,
    val email: String,
    val phone : String,
    val joined : DateTimeFormatter,
    val address : String,
    val profilePicture : ByteArray?,
    val deleted : Boolean
)
