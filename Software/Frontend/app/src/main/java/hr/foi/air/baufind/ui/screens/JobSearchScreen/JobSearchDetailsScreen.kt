package hr.foi.air.baufind.ui.screens.JobSearchScreen

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import hr.foi.air.baufind.R
import hr.foi.air.baufind.helpers.PictureHelper
import hr.foi.air.baufind.navigation.IconType
import hr.foi.air.baufind.ui.components.DisplayTextField
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ui.components.PrimaryTextField
import hr.foi.air.baufind.ws.network.TokenProvider

@Composable
fun JobSearchDetailsScreen(navController: NavController, tokenProvider: TokenProvider, jobSearchViewModel: JobSearchViewModel){
    val selectedJob = jobSearchViewModel.selectedJob.value

    var selectedImageIndex by remember { mutableStateOf<Int?>(null) }

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
            DisplayTextField(title = "Naslov posla", text = selectedJob.title)
            Spacer(modifier = Modifier.height(24.dp))
            DisplayTextField(title = "Opis posla", text = selectedJob.description)
            Spacer(modifier = Modifier.height(24.dp))
            DisplayTextField(title = "Lokacija posla", text = selectedJob.location)
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                text = "Profil poslodavca",
                icon = Icons.Default.Person,
                onClick = { /*ide na profil poslodavca*/}
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
                itemsIndexed(selectedJob.pictures) { index, base64Image ->
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

@Preview(showBackground = true)
@Composable
fun JobSearchDetailsScreenPreview() {
    val navController = rememberNavController()
    JobSearchDetailsScreen(navController, tokenProvider = object : TokenProvider { override fun getToken(): String? { return null } }, jobSearchViewModel = JobSearchViewModel())
}

