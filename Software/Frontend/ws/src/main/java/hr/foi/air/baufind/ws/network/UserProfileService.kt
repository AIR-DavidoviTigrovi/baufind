package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.request.UpdateUserRequest
import hr.foi.air.baufind.ws.response.DeleteUserResponse
import hr.foi.air.baufind.ws.response.UpdateUserResponse
import hr.foi.air.baufind.ws.response.UserProfileWrapper
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserProfileService {
    @GET("users/profile")
    suspend fun getMyUserProfile(): UserProfileWrapper
    @PUT("users/updateProfile")
    suspend fun updateUserProfile(@Body request: UpdateUserRequest): UpdateUserResponse
    @GET("users/delete")
    suspend fun deleteUser(): DeleteUserResponse
    @GET("users/profile/{id}")
    suspend fun getUserProfileById(@Path("id") id: Int): UserProfileWrapper
}