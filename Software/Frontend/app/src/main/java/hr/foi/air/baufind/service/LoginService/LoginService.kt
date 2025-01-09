package hr.foi.air.baufind.service.LoginService

import android.util.Log
import hr.foi.air.baufind.service.PushNotifications.NotificationService
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.request.LoginBody

class LoginService(private val tokenProvider: TokenProvider) {
    suspend fun loginAsync(loginDao: LoginDao): LoginResponse {
        val service = NetworkService.createAuthService(tokenProvider)
        val notificationService = NotificationService()

        val loginBody = LoginBody(
            email = loginDao.email,
            password = loginDao.password,
            firebaseToken = notificationService.getToken()
        )

        return try {
            val response = service.loginUser(loginBody)
            if (response.success.isNotEmpty()) {
                LoginResponse(
                    successfulLogin = true,
                    message = response.success,
                    jwt = response.jwt
                )
            } else {
                LoginResponse(
                    successfulLogin = false,
                    message = response.error,
                    jwt = ""
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LoginResponse(
                successfulLogin = false,
                message = "Pogreška pri fetchanju",
                jwt = ""
            )
        }
    }
}
