package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.request.UpdateUserRequest
import hr.foi.air.baufind.ws.request.UserProfileViewRequest
import hr.foi.air.baufind.ws.response.DeleteUserResponse
import hr.foi.air.baufind.ws.response.UpdateUserResponse
import hr.foi.air.baufind.ws.response.UserProfileResponse
import hr.foi.air.baufind.ws.response.UserProfileWrapper
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserProfileService {
    @GET("users/profile")
    suspend fun getMyUserProfile(): UserProfileWrapper
    @GET("users/profile/{id}")
    suspend fun getUserProfile(@Body request: UserProfileViewRequest) : UserProfileResponse
    @PUT("users/updateProfile")
    suspend fun updateUserProfile(@Body request: UpdateUserRequest): UpdateUserResponse
    @GET("users/delete")
    suspend fun deleteUser(): DeleteUserResponse
}