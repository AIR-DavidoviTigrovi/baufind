package hr.foi.air.baufind.service.WorkerService

import hr.foi.air.baufind.ws.model.User
import hr.foi.air.baufind.ws.model.Worker
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.request.WorkersSkillBody
import hr.foi.air.baufind.ws.response.WorkersSkillResponse

class WorkerSkillService : IWorkerSkillService {
    override suspend fun getWorkersBySkill(workersSkillBody: WorkersSkillBody,tokenProvider: TokenProvider): List<Worker> {
        val service = NetworkService.createWorkersService(tokenProvider)
        try {
            val response = service.getWorkersBySkill(workersSkillBody)
            return response.data
        }catch (err: Exception){
            err.printStackTrace()
            return emptyList()
            }
    }

    override suspend fun getWorkerAccount(worker: Worker,tokenProvider: TokenProvider): User {
        TODO("Not yet implemented")
    }

}