package hr.foi.air.baufind.ws.request

import hr.foi.air.baufind.ws.model.Image

data class WorkerReviewRequest(
    val workingId: Int,
    val comment: String,
    val rating: Int,
    val images: List<Image>? = null
)