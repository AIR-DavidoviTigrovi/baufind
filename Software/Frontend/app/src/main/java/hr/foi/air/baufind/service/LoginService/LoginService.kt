package hr.foi.air.baufind.service.LoginService

import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.request.LoginBody

class LoginService
{
    suspend fun loginAsync(loginDao: LoginDao): LoginResponse {
        val service = NetworkService.authService

        val loginBody = LoginBody(
            email = loginDao.email,
            password = loginDao.password
        )

        try {
            val response = service.loginUser(loginBody)
            if(response.success != "") {
                return LoginResponse(
                    true,
                    response.success,
                    response.jwt
                )
            }
            else {
                return LoginResponse(
                    false,
                    response.error,
                    ""
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return LoginResponse(
                false,
                "Pogreska pri fetchanju",
                ""
            )
        }
    }
}