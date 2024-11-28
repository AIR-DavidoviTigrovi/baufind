package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.model.User
import hr.foi.air.baufind.ws.request.UserProfileBody
import hr.foi.air.baufind.ws.response.UserProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users/{userId}/profile")
    suspend fun getUserById(@Body userProfileBody: UserProfileBody): UserProfileResponse
}