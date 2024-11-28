package hr.foi.air.baufind.ui.screens.JobCreateScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hr.foi.air.baufind.R
import hr.foi.air.baufind.ui.components.PositionAndNumber
import hr.foi.air.baufind.ui.components.PrimaryButton

@Composable
fun JobPositionsLocationScreen(navController: NavController, jobViewModel: JobViewModel){

    fun validateInputs(): Boolean {
        var valid = true

        return valid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Tražene pozicije",
            modifier = Modifier.align(Alignment.Start),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        LazyColumn {
            items(jobViewModel.jobPositions) { position ->
                PositionAndNumber(
                    text = position.name,
                    count = position.count.value,
                    onCountChange = { newCount -> position.count.value = newCount }
                )
            }
        }
        PrimaryButton(
            drawableId = R.drawable.add_person_icon,
            text = "Dodaj poziciju",
            onClick = {
                navController.navigate("jobAddSkillsScreen")
            }
        )
        Text(
            text = "Lokacija posla",
            modifier = Modifier.align(Alignment.Start),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        PrimaryButton(
            text = "Postavi oglas",
            onClick = {
                if (validateInputs()) {
                    val jobDetails = mapOf(
                        "name" to jobViewModel.jobName.value,
                        "description" to jobViewModel.jobDescription.value,
                        "allowInvitations" to jobViewModel.allowInvitations.value,
                        "positions" to jobViewModel.jobPositions.map { "${it.name}: ${it.count}" },
                        "images" to jobViewModel.selectedImages
                    )
                    println(jobDetails)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun JobPositionsLocationScreenPreview() {
    val navController = rememberNavController()
    JobPositionsLocationScreen(navController, JobViewModel())
}