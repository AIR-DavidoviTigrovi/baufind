// ui/screens/RegistrationScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import hr.foi.air.baufind.R
import hr.foi.air.baufind.ui.components.PrimaryButton

@Composable
fun RegistrationScreen() {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    fun validateInputs(): Boolean {
        var valid = true
        emailError = ""
        passwordError = ""
        usernameError = ""
        confirmPasswordError = ""

        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Unesite validan email"
            valid = false
        }
        if (username.isBlank()) {
            usernameError = "Korisničko ime je obvezno"
            valid = false
        }
        /*
        if (username postoji već u bazi)) {
            passwordError = "Korisničko ime je zauzeto"
            valid = false
        }
         */
        if (password.isBlank()) {
            passwordError = "Lozinka je obavezna"
            valid = false
        }
        if (confirmPassword != password || confirmPassword.isBlank()) {
            confirmPasswordError = "Lozinke se ne poklapaju"
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
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError.isNotEmpty(),
            supportingText = { if (emailError.isNotEmpty()) Text(emailError, color = Color.Red) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Korisničko ime") },
            modifier = Modifier.fillMaxWidth(),
            supportingText = { if (usernameError.isNotEmpty()) Text(usernameError, color = Color.Red) }

        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Lozinka") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (isPasswordVisible) {
                    painterResource(id = R.drawable.visibility_icon)
                } else {
                    painterResource(id = R.drawable.off_visibility_icon)
                }

                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(painter = image, contentDescription = "Toggle password visibility")
                }
            },
            isError = passwordError.isNotEmpty(),
            supportingText = { if (passwordError.isNotEmpty()) Text(passwordError, color = Color.Red) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Potvrda lozinke") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = confirmPasswordError.isNotEmpty(),
            supportingText = { if (confirmPasswordError.isNotEmpty()) Text(confirmPasswordError, color = Color.Red) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = "Registracija",
            maxWidth = true,
            onClick = {
                if (validateInputs()) {
                    /*
                    Kad je sve validirano, onda se more napraviti logika
                     */
                }
            }

        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen()
}

