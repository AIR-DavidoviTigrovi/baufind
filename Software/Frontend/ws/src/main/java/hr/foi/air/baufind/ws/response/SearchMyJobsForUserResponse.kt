package hr.foi.air.baufind.ws.response

import hr.foi.air.baufind.ws.model.MyJobModel

data class SearchMyJobsForUserResponse (
    val success: Boolean,
    val jobs : List<MyJobModel>,
    val error : String?
)