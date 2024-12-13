package hr.foi.air.baufind.ws.response

import hr.foi.air.baufind.ws.model.JobSearchModel

data class JobsForCurrentUserResponse(
    val jobs : List<JobSearchModel>,
    val error : String?
)
