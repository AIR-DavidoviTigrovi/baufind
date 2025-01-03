package hr.foi.air.baufind.ui.screens.JobSearchScreen

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hr.foi.air.baufind.core.map.MapProvider
import hr.foi.air.baufind.core.map.models.Coordinates
import hr.foi.air.baufind.helpers.MapHelper
import hr.foi.air.baufind.helpers.PictureHelper
import hr.foi.air.baufind.service.JobGetService.JobGetService
import hr.foi.air.baufind.ui.components.DisplayTextField
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ws.model.FullJobModel
import hr.foi.air.baufind.ws.network.JobService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.response.JobResponse

@Composable
fun JobSearchDetailsScreen(
    navController: NavController,
    tokenProvider: TokenProvider,
    jobSearchViewModel: JobSearchViewModel
){
    val selectedJobId = jobSearchViewModel.selectedJobId
    var selectedJob by remember { mutableStateOf<FullJobModel?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedJobId) {
        if (selectedJobId != 0) {
            isLoading = true
            try {
                val response = JobGetService().fetchJobAsync(selectedJobId, tokenProvider)
                if (response.success) {
                    selectedJob = response.job
                } else {
                    error = response.message
                }
            } catch (e: Exception) {
                Log.e("JobSearchDetailsScreen", "Error fetching job: ${e.message}")
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }

    var coordinates = remember { mutableStateOf(Coordinates(
        selectedJob?.lat ?: 0.0,
        selectedJob?.lng ?: 0.0
    )) }

    var selectedImageIndex by remember { mutableStateOf<Int?>(null) }
    val employerId = selectedJob?.employer_id
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp, 0.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (selectedJob != null) {
            Log.d("JobSearchDetailsScreen", "Selected job unutar ifa: $selectedJob")
            Spacer(modifier = Modifier.height(24.dp))
            DisplayTextField(title = "Naslov posla", text = selectedJob!!.title)
            Spacer(modifier = Modifier.height(24.dp))
            DisplayTextField(title = "Opis posla", text = selectedJob!!.description)
            Spacer(modifier = Modifier.height(24.dp))
            DisplayTextField(title = "Lokacija posla", text = selectedJob!!.location)
            if (selectedJob!!.lat != null && selectedJob!!.lng != null) {
                MapHelper.mapProvider.LocationShowMapScreen(
                    modifier = Modifier,
                    location = selectedJob!!.location,
                    coordinates = coordinates.value
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Profil poslodavca",
                icon = Icons.Default.Person,
                onClick = { navController.navigate("userProfileScreen?userId=$employerId")
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Fotografije",
                modifier = Modifier.align(Alignment.Start),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ){
                itemsIndexed(selectedJob!!.pictures) { index, base64Image ->
                    val imageData = PictureHelper.decodeBase64ToByteArray(base64Image)
                    val bitmap = imageData?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

                    if (bitmap != null) {
                        val imageSize = if(selectedImageIndex == index) 400.dp else 200.dp
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Slika posla",
                            modifier = Modifier
                                .size(imageSize)
                                .padding(8.dp)
                                .clickable{
                                    selectedImageIndex = if(selectedImageIndex == index) null else index
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                text = "Pridruži se poslu",
                icon = Icons.Default.Add,
                onClick = {
                    //salje zahtjev za pridruzenje poslu
                    //jobSearchViewModel.clearData()
                    //navController.navigate()
                }
            )
        }
    }
}

