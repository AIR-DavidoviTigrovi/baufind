package hr.foi.air.baufind.ui.screens.JobSearchScreen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hr.foi.air.baufind.R
import hr.foi.air.baufind.navigation.IconType
import hr.foi.air.baufind.ui.components.DisplayTextField
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ui.components.PrimaryTextField
import hr.foi.air.baufind.ws.network.TokenProvider

@Composable
fun JobSearchDetailsScreen(navController: NavController, tokenProvider: TokenProvider, jobSearchViewModel: JobSearchViewModel){
    val selectedJob = jobSearchViewModel.selectedJob.value
    Log.d("JobSearchDetailsScreen", "Selected job: $selectedJob")

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp,0.dp)
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
                onClick = {}
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Fotografije",
                modifier = Modifier.align(Alignment.Start),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            //tu idu fotografije

            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                text = "Pridruži se poslu",
                icon = Icons.Default.Add,
                onClick = {}
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

