package hr.foi.air.baufind.ui.screens.JobCreateScreen

import android.util.Log
import com.google.gson.Gson
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import hr.foi.air.baufind.service.JobService.JobDao
import hr.foi.air.baufind.service.JobService.JobService
import hr.foi.air.baufind.ui.components.PositionAndNumber
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ui.components.PrimaryTextField
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

@Composable
fun JobPositionsLocationScreen(navController: NavController, jobViewModel: JobViewModel, tokenProvider: TokenProvider){
    jobViewModel.tokenProvider.value = tokenProvider
    val coroutineScope = rememberCoroutineScope()

    var latError by remember { mutableStateOf("") }
    var longError by remember { mutableStateOf("") }
    var context = LocalContext.current
    val gson = Gson()
    var latText by remember { mutableStateOf("") }
    var longText by remember { mutableStateOf("") }

    fun validateInputs(): Boolean {
        var valid = true

        latError = ""
        longError = ""

        if (latText.isEmpty() || latText.toDoubleOrNull() == null) {
            latError = "Morate unijeti validan lat"
            valid = false
        }
        if (longText.isEmpty() || longText.toDoubleOrNull() == null) {
            longError = "Morate unijeti validan long"
            valid = false
        }
        if (jobViewModel.jobPositions.isEmpty()) {
            Toast.makeText(context, "Morate unijeti bar jednu poziciju", Toast.LENGTH_SHORT).show()
            valid = false
        }
        return valid
    }

    fun updateCoordinates() {
        val latValue = latText.toDoubleOrNull() ?: 0.0
        val longValue = longText.toDoubleOrNull() ?: 0.0
        jobViewModel.lat.doubleValue = latValue
        jobViewModel.long.doubleValue = longValue
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
        Spacer(modifier = Modifier.height(24.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(jobViewModel.jobPositions) { position ->
                PositionAndNumber(
                    text = position.name,
                    count = position.count.value,
                    onCountChange = { newCount -> position.count.value = newCount },
                    onDelete = { jobViewModel.jobPositions.remove(position) }
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            drawableId = R.drawable.add_person_icon,
            text = "Dodaj poziciju",
            onClick = {
                navController.navigate("jobAddSkillsScreen")
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Lokacija posla",
            modifier = Modifier.align(Alignment.Start),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryTextField(
            value = latText,
            onValueChange = { latText = it },
            label = "Lat",
            modifier = Modifier.fillMaxWidth(),
            isError = latError.isNotEmpty(),
            errorMessage = latError
        )
        PrimaryTextField(
            value = longText,
            onValueChange = { longText = it },
            label = "Lng",
            modifier = Modifier.fillMaxWidth(),
            isError = longError.isNotEmpty(),
            errorMessage = longError
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = "Postavi oglas",
            onClick = {
                if (validateInputs()) {
                    updateCoordinates()
                    val service = JobService()
                    coroutineScope.launch{
                        val response = service.addJobAsync(
                            JobDao(
                                name = jobViewModel.jobName.value,
                                description = jobViewModel.jobDescription.value,
                                allowInvitations = jobViewModel.allowInvitations.value,
                                positions = jobViewModel.getPositionsArray(),
                                images = jobViewModel.getImagesAsByteArrayList(context),
                                lat = jobViewModel.lat.doubleValue,
                                long = jobViewModel.long.doubleValue
                            ),
                            tokenProvider
                        )
                        if (response.added) {
                            var positions = gson.toJson(jobViewModel.getPositionsArray())
                            Log.d("ugbug12", "Position: $positions")
                            navController.navigate("workersSearchScreen/${positions}")
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun JobPositionsLocationScreenPreview() {
    val navController = rememberNavController()
    JobPositionsLocationScreen(navController, JobViewModel(), object : TokenProvider { override fun getToken(): String? { return null } })
}