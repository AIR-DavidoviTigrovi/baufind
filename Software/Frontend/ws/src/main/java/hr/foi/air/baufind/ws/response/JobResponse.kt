package hr.foi.air.baufind.ws.response

import hr.foi.air.baufind.ws.model.FullJobModel

data class JobResponse(
    val job : FullJobModel,
    val error : String?
)
