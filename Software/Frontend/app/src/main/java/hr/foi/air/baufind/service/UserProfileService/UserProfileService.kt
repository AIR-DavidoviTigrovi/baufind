package hr.foi.air.baufind.service.UserProfileService

import retrofit2.HttpException
import hr.foi.air.baufind.ws.network.UserProfileService
import hr.foi.air.baufind.ws.response.UserProfileResponse
import java.io.IOException

class UserProfileService(private val userProfileNetworkService: UserProfileService){
    suspend fun fetchUserProfile(jwt: String): UserProfileResponse? {
        return try {
            userProfileNetworkService.getMyUserProfile()
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}