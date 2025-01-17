package hr.foi.air.baufind.ui.screens.JobHistoryScreen

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import hr.foi.air.baufind.ui.components.DisplayTextField
import hr.foi.air.baufind.ui.components.JobEventItem
import hr.foi.air.baufind.ui.components.PersonInRoomCard
import hr.foi.air.baufind.ws.network.TokenProvider

@Composable
fun SelectedJobHistoryScreen(
    navController: NavController,
    tokenProvider: TokenProvider,
    selectedJobHistoryViewModel: SelectedJobHistoryViewModel
){
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry){
        selectedJobHistoryViewModel.clearData()
        selectedJobHistoryViewModel.tokenProvider = tokenProvider

    }

    val scrollState = rememberScrollState()

    if(selectedJobHistoryViewModel.isLoading()){
        Text(text = "Učitavam...")
    }
    else if(selectedJobHistoryViewModel.hasError()){
        Text(text = selectedJobHistoryViewModel.message!!)
    }
    else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = selectedJobHistoryViewModel.job.value!!.title,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Informacije o poslu",
                modifier = Modifier.align(Alignment.Start),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            DisplayTextField(
                title = "Opis",
                text = selectedJobHistoryViewModel.job.value!!.description,
                modifier = Modifier.fillMaxWidth()
            )
            DisplayTextField(
                title = "Lokacija",
                text = selectedJobHistoryViewModel.job.value!!.location,
                modifier = Modifier.fillMaxWidth()
            )
            DisplayTextField(
                title = "Vlasnik posla",
                text = selectedJobHistoryViewModel.job.value!!.ownerName,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Radnici na poslu",
                modifier = Modifier.align(Alignment.Start),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            selectedJobHistoryViewModel.job.value!!.workers.forEach { worker ->
                PersonInRoomCard(workerName = worker.roleTitle + " - " + worker.workerName, onItemClick = {})
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Slijed događaja na poslu",
                modifier = Modifier.align(Alignment.Start),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = Modifier.align(Alignment.Start),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                selectedJobHistoryViewModel.job.value!!.events.forEach { event ->
                    JobEventItem(
                        eventName = event.eventName,
                        eventDate = event.date
                    )
                }
            }
        }
    }
}