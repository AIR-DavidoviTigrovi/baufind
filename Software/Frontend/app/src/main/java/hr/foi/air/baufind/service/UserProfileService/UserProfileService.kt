package hr.foi.air.baufind.service.UserProfileService

import android.net.http.HttpException
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.request.UpdateUserRequest
import hr.foi.air.baufind.ws.request.UserProfileViewRequest
import hr.foi.air.baufind.ws.response.DeleteUserResponse
import hr.foi.air.baufind.ws.response.UpdateUserResponse
import hr.foi.air.baufind.ws.response.UserProfileResponse
import retrofit2.HttpException
import java.io.IOException

class UserProfileService(tokenProvider: TokenProvider){
    private val userProfileNetworkService = NetworkService.createUserProfileService(tokenProvider)
    suspend fun fetchUserProfile(): UserProfileResponse? {
        return try {
            val wrapper = userProfileNetworkService.getMyUserProfile()
            if (wrapper.error.isNullOrEmpty()) {
                wrapper.userProfileModel
            } else {
                println("Error from backend: ${wrapper.error}")
                null
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    suspend fun updateUserProfile(request: UpdateUserRequest): UpdateUserResponse? {
        return try {
            userProfileNetworkService.updateUserProfile(request)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    suspend fun deleteUser(): DeleteUserResponse? {
        return try{
            userProfileNetworkService.deleteUser()
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    suspend fun fetchUserViewProfile(request: UserProfileViewRequest): UserProfileResponse? {
        try {
            val response = userProfileNetworkService.getUserProfile(request)
            return response
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

}