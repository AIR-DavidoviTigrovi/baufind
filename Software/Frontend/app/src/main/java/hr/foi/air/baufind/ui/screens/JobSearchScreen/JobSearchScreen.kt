package hr.foi.air.baufind.ui.screens.JobSearchScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import hr.foi.air.baufind.ui.components.JobListItem
import hr.foi.air.baufind.ui.components.PrimaryTextField
import hr.foi.air.baufind.ws.network.TokenProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSearchScreen(navController: NavController, tokenProvider: TokenProvider, jobSearchViewModel: JobSearchViewModel, jobSearchDetailsViewModel : JobSearchDetailsViewModel){

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry){
        jobSearchViewModel.clearData()
        jobSearchViewModel.tokenProvider = tokenProvider
    }

    var searchText by remember { mutableStateOf("") }

    var selectedCounty by remember { mutableStateOf<String?>(null) }
    var showCountyBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var searchQuery by remember { mutableStateOf("") }

    val allJobs = jobSearchViewModel.jobs.value

    val jobsFilteredByCounty = allJobs.filter { job ->
        val countyInEnglish = jobSearchViewModel.getEnglishCounty(selectedCounty ?: "")
        countyInEnglish == null || job.location.contains(countyInEnglish, ignoreCase = true)
    }

    val filteredJobs = jobsFilteredByCounty.filter { job ->
        job.title.contains(searchText, ignoreCase = true) ||
                job.skills.any { skill -> skill.title.contains(searchText, ignoreCase = true) }
    }

    val croatianCounties = jobSearchViewModel.croatianCounties
    val filteredCounties = croatianCounties.filter{
        it.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Column{
                Button(onClick = { showCountyBottomSheet = true }) {
                    Text(text = selectedCounty ?: "Odaberite županiju")
                }
                if(selectedCounty != null){
                    Button(onClick = { selectedCounty = null }) {
                        Text(text = "Očisti odabir")
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {navController.navigate("jobNotificationScreen") }
            ) {
                Icon(

                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))


        }
        if(showCountyBottomSheet){
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { showCountyBottomSheet = false }
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        placeholder = { Text("Pretraži županiju")},
                        singleLine = true,
                        maxLines = 1
                    )
                    Box(modifier = Modifier.fillMaxWidth()){
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 32.dp)
                        ) {
                            items(filteredCounties.size) { index ->
                                val county = filteredCounties[index]
                                Text(
                                    text = county,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .clickable {
                                            selectedCounty = county
                                            showCountyBottomSheet = false
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
        PrimaryTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = "Pretraži po imenu ili pozicijama",
            modifier = Modifier.fillMaxWidth(),
            isError = false
        )
        if(jobSearchViewModel.isLoading()){
            Text(text = "Učitavam...")
        }else if(jobSearchViewModel.hasError()){
            Text(text = jobSearchViewModel.message!!)
        }
        else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(filteredJobs.size) { index ->
                    val job = filteredJobs[index]
                    JobListItem(job = job){
                        jobSearchDetailsViewModel.selectedJobId = job.id
                        navController.navigate("jobSearchDetailsScreen")
                    }
                }
            }
        }
    }
}