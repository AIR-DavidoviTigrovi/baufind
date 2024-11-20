package hr.foi.air.baufind.service.RegistrationService

import hr.foi.air.baufind.model.RegistrationDao
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.request.RegistrationBody

class RegistrationService()
{
    suspend fun addNewUserAsync(registrationDao: RegistrationDao): String {
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
            if(response.user != null) return response.success
            else return response.error
        } catch (e: Exception) {
            e.printStackTrace()
            return "Pogreska pri fetchanju"
        }
    }
}
