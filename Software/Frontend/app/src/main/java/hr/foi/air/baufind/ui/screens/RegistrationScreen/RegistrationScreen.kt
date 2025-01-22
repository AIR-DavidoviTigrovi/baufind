// ui/screens/RegistrationScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.foi.air.baufind.service.RegistrationService.RegistrationDao
import hr.foi.air.baufind.service.RegistrationService.RegistrationService
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ui.components.PrimaryTextField
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(navController : NavController, tokenProvider: TokenProvider) {
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var registrationError by remember { mutableStateOf("") }
    fun validateInputs(): Boolean {
        var valid = true
        emailError = ""
        nameError = ""
        phoneError = ""
        addressError = ""
        passwordError = ""
        confirmPasswordError = ""

        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Morate unijeti email"
            valid = false
        }
        if (name.isBlank()) {
            nameError = "Morate unijeti ime"
            valid = false
        }
        if (phone.isBlank()) {
            phoneError = "Morate unijeti broj telefona"
            valid = false
        }
        if (address.isBlank()) {
            addressError = "Morate unijeti adresu"
            valid = false
        }
        if (password.isBlank()) {
            passwordError = "Morate unijeti lozinku"
            valid = false
        }
        if (confirmPassword != password || confirmPassword.isBlank()) {
            confirmPasswordError = "Lozinke se ne podudaraju"
            valid = false
        }
        return valid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text ="Baufind",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text ="Započnite koristiti Baufind",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))


        PrimaryTextField(
            value = name,
            onValueChange = { name = it },
            label ="Ime i prezime",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = nameError.isNotEmpty(),
            errorMessage = nameError
        )


        Spacer(modifier = Modifier.height(8.dp))
        PrimaryTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError.isNotEmpty(),
            errorMessage = emailError
        )

        Spacer(modifier = Modifier.height(8.dp))
        PrimaryTextField(
            value = phone,
            onValueChange = { phone = it },
            label = "Broj telefona",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = phoneError.isNotEmpty(),
            errorMessage = phoneError
        )
        Spacer(modifier = Modifier.height(8.dp))
        PrimaryTextField(
            value = address,
            onValueChange = { address = it },
            label = "Adresa",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = addressError.isNotEmpty(),
            errorMessage = addressError
        )


        Spacer(modifier = Modifier.height(8.dp))

        PrimaryTextField(
            value = password,
            onValueChange = { password = it },
            label = "Lozinka",
            modifier = Modifier.fillMaxWidth(),
            visualTransformation =  PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError.isNotEmpty(),
            errorMessage = passwordError
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrimaryTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Ponovite lozinku",
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = confirmPasswordError.isNotEmpty(),
            errorMessage = confirmPasswordError
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text ="Pritiskom na \"Registriraj se\" slažete se s našim Uvjetima i Politikom privatnosti.",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text =registrationError,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .then(Modifier.padding(bottom = 16.dp)),
            fontSize = 16.sp,
            color = Color.Red
        )
        PrimaryButton(
            text = "Registriraj se",
            maxWidth = true,
            onClick = {
                if (validateInputs()) {
                    val service = RegistrationService()
                    coroutineScope.launch {
                        val response = service.addNewUserAsync(
                            RegistrationDao(
                                name = name,
                                email = email,
                                phone = phone,
                                address = address,
                                password = password,
                                confirmPassword = confirmPassword
                            ),
                            tokenProvider
                        )
                        if (response.added) navController.navigate("login")
                        else registrationError = "Nemogućnost registracije s tim podacima"
                    }
                }
            }
        )
    }
}


