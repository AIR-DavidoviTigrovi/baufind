package hr.foi.air.baufind.service.WorkerService

import hr.foi.air.baufind.ws.model.User
import hr.foi.air.baufind.ws.model.Worker
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.request.WorkersSkillBody
import hr.foi.air.baufind.ws.response.WorkersSkillResponse

interface IWorkerSkillService {

    suspend fun getWorkersBySkill(ids: String,workersSkillBody: WorkersSkillBody, tokenProvider: TokenProvider): List<Worker>
    suspend fun getWorkerAccount(worker: Worker, tokenProvider: TokenProvider): User
    suspend fun callWorkerToJob(request: CallForWorkingRequest, tokenProvider: TokenProvider):CallForWorkingResponse
}