package hr.foi.air.baufind.ui.screens.JobSearchScreen

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hr.foi.air.baufind.core.map.MapProvider
import hr.foi.air.baufind.core.map.models.Coordinates
import hr.foi.air.baufind.helpers.MapHelper
import hr.foi.air.baufind.helpers.PictureHelper
import hr.foi.air.baufind.service.JobGetService.JobGetService
import hr.foi.air.baufind.service.WorkerRequestJoinService.WorkerRequestJoinService
import hr.foi.air.baufind.ui.components.DisplayTextField
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ui.components.SkillListConfirm
import hr.foi.air.baufind.ws.model.FullJobModel
import hr.foi.air.baufind.ws.network.JobService
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.response.JobResponse
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun JobSearchDetailsScreen(
    navController: NavController,
    tokenProvider: TokenProvider,
    jobSearchDetailsViewModel: JobSearchDetailsViewModel
){

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry) {
        jobSearchDetailsViewModel.clearData()
        jobSearchDetailsViewModel.tokenProvider = tokenProvider
    }

    var coordinates = remember { mutableStateOf(Coordinates(
        jobSearchDetailsViewModel.job?.lat ?: 0.0,
        jobSearchDetailsViewModel.job?.lng ?: 0.0
    )) }

    var selectedImageIndex by remember { mutableStateOf<Int?>(null) }
    val employerId = jobSearchDetailsViewModel.job?.employer_id
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    fun sendJoinRequest(skillId: Int){
        coroutineScope.launch{
            val service = WorkerRequestJoinService()
            val response = service.sendJoinRequestAsync(
                skillId = skillId,
                jobId = jobSearchDetailsViewModel.job!!.id,
                tokenProvider = tokenProvider
            )
            if(response.success){
                jobSearchDetailsViewModel.clearData()
                navController.navigate("pendingJobsScreen"){
                    popUpTo("jobSearchDetailsScreen") { inclusive = true }
                }
            }else{
                Toast.makeText(context, "Dogodila se pogreška.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp, 0.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if(jobSearchDetailsViewModel.isLoading()){
            Text(text = "Učitavam...")
        }else if(jobSearchDetailsViewModel.HasError()){
            Text(text = jobSearchDetailsViewModel.message!!)
        }
        else{
            Log.d("JobSearchDetailsScreen", "Selected job unutar ifa: $jobSearchDetailsViewModel.job")
            Spacer(modifier = Modifier.height(24.dp))
            DisplayTextField(title = "Naslov posla", text = jobSearchDetailsViewModel.job!!.title)
            Spacer(modifier = Modifier.height(24.dp))
            DisplayTextField(title = "Opis posla", text = jobSearchDetailsViewModel.job!!.description)
            Spacer(modifier = Modifier.height(24.dp))
            DisplayTextField(title = "Lokacija posla", text = jobSearchDetailsViewModel.job!!.location)
            if (jobSearchDetailsViewModel.job!!.lat != null && jobSearchDetailsViewModel.job!!.lng != null) {
                MapHelper.mapProvider.LocationShowMapScreen(
                    modifier = Modifier,
                    location = jobSearchDetailsViewModel.job!!.location,
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
                itemsIndexed(jobSearchDetailsViewModel.job!!.pictures) { index, base64Image ->
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
                                .clickable {
                                    selectedImageIndex =
                                        if (selectedImageIndex == index) null else index
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Otvorene pozicije za Vas",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                jobSearchDetailsViewModel.job!!.skills.forEach { skill ->
                    SkillListConfirm(text = skill.title) {
                        sendJoinRequest(skill.id)
                    }
                }
            }
        }
    }
}