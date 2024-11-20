package hr.foi.air.baufind.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

data class RegistrationDao(
    val name : String,
    val email : String,
    val phone : String,
    val address : String,
    val password : String,
    val confirmPassword : String
)