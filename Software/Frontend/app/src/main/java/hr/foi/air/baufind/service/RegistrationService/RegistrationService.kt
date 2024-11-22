package hr.foi.air.baufind.service.RegistrationService

import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.request.RegistrationBody

class RegistrationService()
{
    suspend fun addNewUserAsync(registrationDao: RegistrationDao): RegistrationResponse {
        val service = NetworkService.authService

        val registrationBody = RegistrationBody(
            name = registrationDao.name,
            email = registrationDao.email,
            phone = registrationDao.phone,
            address = registrationDao.address,
            password = registrationDao.password,
            repeatPassword = registrationDao.confirmPassword
        )

        try {
            val response = service.registerUser(registrationBody)
            if(response.success != "") {
                return RegistrationResponse(
                    true,
                    response.success
                )
            }
            else {
                return RegistrationResponse(
                    false,
                    response.error
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return RegistrationResponse(
                false,
                "Pogreska pri fetchanju"
            )
        }
    }
}
