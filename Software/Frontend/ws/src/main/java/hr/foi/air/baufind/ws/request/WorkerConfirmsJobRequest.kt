package hr.foi.air.baufind.ws.request

data class WorkerConfirmsJobRequest (
    var ConfirmWorkerRequest : ConfirmWorkerRequest,
    var EmployerIdForNotification: Int
)