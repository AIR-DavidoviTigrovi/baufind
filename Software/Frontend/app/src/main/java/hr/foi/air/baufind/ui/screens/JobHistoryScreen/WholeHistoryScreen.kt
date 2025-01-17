package hr.foi.air.baufind.ui.screens.JobHistoryScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hr.foi.air.baufind.ui.components.HistoryListJobItem
import hr.foi.air.baufind.ws.network.TokenProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WholeHistoryScreen(
    navController: NavController,
    tokenProvider: TokenProvider,
    wholeHistoryViewModel: WholeHistoryViewModel,
    selectedJobHistoryViewModel: SelectedJobHistoryViewModel
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry){
        wholeHistoryViewModel.clearData()
        wholeHistoryViewModel.tokenProvider = tokenProvider
    }

    Scaffold(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            ) {
                if (wholeHistoryViewModel.isLoading()) {
                    Text(text = "Učitavam...", modifier = Modifier.padding(16.dp))
                } else if (wholeHistoryViewModel.hasError()) {
                    Text(text = wholeHistoryViewModel.message ?: "Došlo je do pogreške", modifier = Modifier.padding(16.dp))
                } else {
                    val ownerJobs = wholeHistoryViewModel.jobs.value.filter { it.isOwner }
                    val workerJobs = wholeHistoryViewModel.jobs.value.filter { !it.isOwner }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (ownerJobs.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Poslovi na kojima sam bio vlasnik",
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(bottom = 8.dp),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            items(ownerJobs) { job ->
                                HistoryListJobItem(
                                    picture = job.picture,
                                    name = job.title,
                                    date = job.completionDate,
                                    onItemClick = {
                                        selectedJobHistoryViewModel.selectedJobId.value = job.jobId
                                        navController.navigate("selectedJobHistoryScreen")
                                    }
                                )
                            }
                        }

                        if (workerJobs.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Poslovi na kojima sam bio radnik",
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(top = 16.dp, bottom = 8.dp),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            items(workerJobs) { job ->
                                HistoryListJobItem(
                                    picture = job.picture,
                                    name = job.title,
                                    date = job.completionDate,
                                    onItemClick = {
                                        selectedJobHistoryViewModel.selectedJobId.value = job.jobId
                                        navController.navigate("selectedJobHistoryScreen")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}