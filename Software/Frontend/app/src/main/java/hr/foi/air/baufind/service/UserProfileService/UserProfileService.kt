package hr.foi.air.baufind.service.UserProfileService

import android.content.ContentValues.TAG
import android.util.Log
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.request.UpdateUserRequest
import hr.foi.air.baufind.ws.response.UpdateUserResponse
import hr.foi.air.baufind.ws.response.UserProfileResponse
import retrofit2.HttpException
import java.io.IOException

class UserProfileService(private val tokenProvider: TokenProvider){
    suspend fun fetchUserProfile(): UserProfileResponse? {
        val userProfileNetworkService = NetworkService.createUserProfileService(tokenProvider)
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
        Log.d(TAG, request.toString())
        val userProfileNetworkService = NetworkService.createUserProfileService(tokenProvider)
        return try {
            userProfileNetworkService.updateUserProfile(request)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}