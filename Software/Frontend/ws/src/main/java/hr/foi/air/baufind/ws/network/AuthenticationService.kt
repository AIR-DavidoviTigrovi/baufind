package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.request.LoginBody
import hr.foi.air.baufind.ws.request.RegistrationBody
import hr.foi.air.baufind.ws.response.LoginResponse
import hr.foi.air.baufind.ws.response.RegistrationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {
    @POST("users/register")
    suspend fun registerUser(@Body registerBody: RegistrationBody): RegistrationResponse

    @POST("users/login")
    suspend fun loginUser(@Body loginBody: LoginBody) : LoginResponse

}
