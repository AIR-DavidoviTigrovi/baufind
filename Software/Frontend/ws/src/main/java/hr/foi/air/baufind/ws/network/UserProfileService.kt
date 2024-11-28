package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.response.UserProfileWrapper
import retrofit2.http.GET
import retrofit2.http.Header

interface UserProfileService {
    @GET("users/profile")
    suspend fun getMyUserProfile(): UserProfileWrapper
}