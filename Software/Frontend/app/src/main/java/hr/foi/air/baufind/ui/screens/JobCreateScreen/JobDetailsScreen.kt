package hr.foi.air.baufind.ui.screens.JobCreateScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ui.components.PrimaryTextField

//ekran za dodavanje naslova, opisa, fotografija i toggle za dopuštanje pozivanja od strane radnika
@Composable
fun JobDetailsScreen(navController: NavController){
    var jobName by remember { mutableStateOf("") }
    var jobNameError by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }
    var jobDescriptionError by remember { mutableStateOf("") }
    var allowInvitations by remember { mutableStateOf(false) }

    fun validateInputs(): Boolean {
        var valid = true

        jobNameError =""
        jobDescriptionError=""

        if (jobName.isBlank()) {
            jobNameError = "You must enter your email"
            valid = false
        }
        if (jobDescription.isBlank()) {
            jobDescriptionError = "You must enter your password"
            valid = false
        }
        return valid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ){
        PrimaryTextField(
            value = jobName,
            onValueChange = { jobName = it },
            label = "Naslov posla",
            modifier = Modifier.fillMaxWidth(),
            isError = jobNameError.isNotEmpty(),
            errorMessage = jobNameError
        )
        PrimaryTextField(
            value = jobDescription,
            onValueChange = { jobDescription = it },
            label = "Opis posla",
            modifier = Modifier.fillMaxWidth(),
            isError = jobDescriptionError.isNotEmpty(),
            errorMessage = jobDescriptionError
        )
        PrimaryButton(
            text = "Učitaj fotografije",
            maxWidth = true,
            onClick = {
                //Ovdje ide logika za učitavanje fotografija
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun JobDetailsScreenPreview() {
    val navController = rememberNavController()
    JobDetailsScreen(navController)
}