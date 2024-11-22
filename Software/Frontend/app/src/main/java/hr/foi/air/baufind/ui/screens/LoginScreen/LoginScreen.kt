package hr.foi.air.baufind.ui.screens.LoginScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.foi.air.baufind.service.LoginService.LoginDao
import hr.foi.air.baufind.service.LoginService.LoginService
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ui.components.PrimaryTextField
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var loginError by remember { mutableStateOf("") }


    fun validateInputs(): Boolean {
        var valid = true

        emailError =""
        passwordError=""

        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "You must enter your email"
            valid = false
        }
        if (password.isBlank()) {
            passwordError = "You must enter your password"
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
            text ="Welcome back",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

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
            value = password,
            onValueChange = { password = it },
            label = "Password",
            modifier = Modifier.fillMaxWidth(),
            visualTransformation =  PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError.isNotEmpty(),
            errorMessage = passwordError
        )
        Text(
            text ="Forgot password?",
            modifier = Modifier.align(Alignment.Start).clickable {
                //Ovdje ide logika za reset lozinke
            },
            fontSize = 14.sp,
            fontWeight = FontWeight.Thin,
            textDecoration = TextDecoration.Underline
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = loginError,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 16.sp,
            color = Color.Red
        )
        PrimaryButton(
            text = "Log in",
            maxWidth = true,
            onClick = {
                if (validateInputs()) {
                    val service = LoginService()
                    coroutineScope.launch {
                        val response = service.loginAsync(
                            LoginDao(
                                email = email,
                                password = password
                            )
                        )
                        if (response.successfulLogin) navController.navigate("workersSearchScreen")
                        else {
                            email = ""
                            password =""
                            loginError = "invalid username and password"
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text ="New user? Sign up",
            modifier = Modifier.align(Alignment.CenterHorizontally).clickable { navController.navigate("registration") },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}