package hr.foi.air.baufind.service.JobSearchService

import hr.foi.air.baufind.ws.model.JobSearchModel

data class JobSearchResponse(
    val success : Boolean,
    val message : String?,
    val jobs : List<JobSearchModel>
)