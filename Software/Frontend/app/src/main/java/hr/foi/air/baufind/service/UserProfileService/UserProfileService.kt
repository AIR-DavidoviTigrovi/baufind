package hr.foi.air.baufind.service.UserProfileService

import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.response.UserProfileResponse

class UserProfileService(private val networkService: NetworkService){
    suspend fun fetchUserProfile() {
        //napraviti request
    }
}