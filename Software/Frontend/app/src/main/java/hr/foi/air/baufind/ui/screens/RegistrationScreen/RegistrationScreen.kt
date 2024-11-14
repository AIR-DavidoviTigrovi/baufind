// ui/screens/RegistrationScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import hr.foi.air.baufind.R
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ui.components.PrimaryTextField

@Composable
fun RegistrationScreen() {
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

    fun validateInputs(): Boolean {
        var valid = true
        emailError = ""
        nameError = ""
        phoneError = ""
        addressError = ""
        passwordError = ""
        confirmPasswordError = ""

        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "You must enter your email"
            valid = false
        }
        if (name.isBlank()) {
            nameError = "You must enter your name"
            valid = false
        }
        if (phone.isBlank()) {
            phoneError = "You must enter your phone number"
            valid = false
        }
        if (address.isBlank()) {
            addressError = "You must enter your address"
            valid = false
        }
        /*
        if (username postoji već u bazi)) {
            passwordError = "Korisničko ime je zauzeto"
            valid = false
        }
         */
        if (password.isBlank()) {
            passwordError = "You must insert your password"
            valid = false
        }
        if (confirmPassword != password || confirmPassword.isBlank()) {
            confirmPasswordError = "Passwords do not match"
            valid = false
        }
        return valid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text ="Baufind",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text ="Get started with Baufind",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))


        PrimaryTextField(
            value = name,
            onValueChange = { name = it },
            label ="Name",
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
            label = "Phone number",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = phoneError.isNotEmpty(),
            errorMessage = phoneError
        )
        Spacer(modifier = Modifier.height(8.dp))
        PrimaryTextField(
            value = address,
            onValueChange = { address = it },
            label = "Address",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = addressError.isNotEmpty(),
            errorMessage = addressError
        )


        Spacer(modifier = Modifier.height(8.dp))

        PrimaryTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
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
            label = "Repeat password",
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = confirmPasswordError.isNotEmpty(),
            errorMessage = confirmPasswordError
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text ="By tapping Register, you agree to our Terms and Privacy Policy.",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = "Register",
            maxWidth = true,
            onClick = {
                if (validateInputs()) {
                    /*
                    Kad je sve validirano, onda se more napraviti logika
                     */
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            drawableId = R.drawable.google_icon,
            text = "Register with google",
            maxWidth = true,
            onClick = {
                if (validateInputs()) {
                }
            }
        )
    }
}


