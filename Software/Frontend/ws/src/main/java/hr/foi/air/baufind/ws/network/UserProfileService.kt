package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.request.UpdateUserRequest
import hr.foi.air.baufind.ws.response.UpdateUserResponse
import hr.foi.air.baufind.ws.response.UserProfileWrapper
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface UserProfileService {
    @GET("users/profile")
    suspend fun getMyUserProfile(): UserProfileWrapper
    @PUT("users/updateProfile")
    suspend fun updateUserProfile(@Body request: UpdateUserRequest): UpdateUserResponse

}