package hr.foi.air.baufind.service.WorkerService

import hr.foi.air.baufind.ws.model.User
import hr.foi.air.baufind.ws.model.Worker
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.request.CallForWorkingBody
import hr.foi.air.baufind.ws.request.WorkersSkillBody
import hr.foi.air.baufind.ws.response.WorkersSkillResponse

class WorkerSkillService : IWorkerSkillService {
    override suspend fun getWorkersBySkill(workersSkillBody: WorkersSkillBody,tokenProvider: TokenProvider): List<Worker> {
        val service = NetworkService.createWorkersService(tokenProvider)
        try {
            val response = service.getWorkersBySkill(workersSkillBody.title)
            return response.workerRecords
        }catch (err: Exception){
            err.printStackTrace()
            return emptyList()
            }
    }

    override suspend fun callWorkerToJob(
        request: CallForWorkingRequest,
        tokenProvider: TokenProvider
    ): CallForWorkingResponse {
        val service = NetworkService.createJobService(tokenProvider)
        try {
            val response = service.callForWorking(
                CallForWorkingBody(
                    request.workerId,
                    request.jobId,
                    request.skillId
                )
            )
            return CallForWorkingResponse(
                response.success,
                response.message
            )
        }catch (err: Exception){
            err.printStackTrace()
            return CallForWorkingResponse(
                message = "Greška prilikom fetchanja",
                success = false
            )
        }
    }

    override suspend fun getWorkerAccount(worker: Worker,tokenProvider: TokenProvider): User {
        TODO("Not yet implemented")
    }


}