package hr.foi.air.baufind.ui.screens.JobCreateScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.google.gson.Gson
import hr.foi.air.baufind.R
import hr.foi.air.baufind.core.map.MapProvider
import hr.foi.air.baufind.core.map.models.Coordinates
import hr.foi.air.baufind.helpers.MapHelper
import hr.foi.air.baufind.service.JobService.JobDao
import hr.foi.air.baufind.service.JobService.JobService
import hr.foi.air.baufind.ui.components.ExpandableText
import hr.foi.air.baufind.ui.components.PositionAndNumber
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ui.components.PrimaryTextField
import hr.foi.air.baufind.ws.network.NetworkService
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

@Composable
fun JobPositionsLocationScreen(
    navController: NavController,
    jobViewModel: JobViewModel,
    tokenProvider: TokenProvider
){
    jobViewModel.tokenProvider.value = tokenProvider
    val coroutineScope = rememberCoroutineScope()

    var context = LocalContext.current
    val gson = Gson()

    var coordinates = remember { mutableStateOf(Coordinates(45.33295293903444, 17.702489909850566)) }
    var locationText by remember { mutableStateOf("") }

    val geocodingService = NetworkService.createGeocodingService()
    var geocodedLocation by remember { mutableStateOf("") }
    var locationLoaded = true

    fun validateInputs(): Boolean {
        if (!locationLoaded || geocodedLocation.isBlank()) {
            Toast.makeText(context, "Morate unijeti ispravnu lokaciju", Toast.LENGTH_SHORT).show()
            return false
        }
        if (jobViewModel.jobPositions.isEmpty()) {
            Toast.makeText(context, "Morate unijeti bar jednu poziciju", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    fun getEntireLocation(): String {
        var locationMiddle = ""
        if (locationText.isNotEmpty() && geocodedLocation.isNotEmpty()) {
            locationMiddle = ", "
        }
        return "$locationText$locationMiddle$geocodedLocation"
    }

    fun updateLocation() {
        jobViewModel.location.value = getEntireLocation()
        jobViewModel.lat.doubleValue = coordinates.value.lat
        jobViewModel.long.doubleValue = coordinates.value.long
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
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                jobViewModel.jobPositions.forEach { position ->
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
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Lokacija posla",
            modifier = Modifier.align(Alignment.Start),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))
        MapHelper.mapProvider.LocationPickerMapScreen(
            modifier = Modifier,
            coordinates = coordinates.value,
            onCoordinatesChanged = { loc ->
                coordinates.value = loc
                coroutineScope.launch {
                    if (!locationLoaded) {
                        return@launch
                    }
                    geocodedLocation = "Učitavam lokaciju..."
                    locationLoaded = false
                    try {
                        val geocodingResponse = geocodingService.reverseGeocode(loc.lat, loc.long)
                        geocodedLocation = geocodingResponse.location ?: "Nepoznata"
                    } catch (e: Exception) {
                        geocodedLocation = "Greška"
                    } finally {
                        locationLoaded = true
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        PrimaryTextField(
            value = locationText,
            onValueChange = { locationText = it },
            label = "Dodatno o lokaciji (neobavezno)",
            modifier = Modifier.fillMaxWidth(),
            isError = false
        )
        Spacer(modifier = Modifier.height(8.dp))
        ExpandableText(
            modifier = Modifier.fillMaxWidth(),
            text = getEntireLocation()
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = "Postavi oglas",
            onClick = {
                if (validateInputs()) {
                    updateLocation()
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
                                long = jobViewModel.long.doubleValue,
                                location = jobViewModel.location.value
                            ),
                            tokenProvider
                        )
                        if (response.added) {
                            var positions = gson.toJson(jobViewModel.getPositionsArray())
                            jobViewModel.clearData()
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
    JobPositionsLocationScreen(
        navController,
        JobViewModel(),
        object : TokenProvider { override fun getToken(): String? { return null } }
    )
}