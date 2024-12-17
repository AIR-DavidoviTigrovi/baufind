package hr.foi.air.baufind.ui.screens.JobSearchScreen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hr.foi.air.baufind.service.JobSearchService.JobSearchResponse
import hr.foi.air.baufind.service.JobSearchService.JobSearchService
import hr.foi.air.baufind.ui.components.JobListItem
import hr.foi.air.baufind.ui.components.PrimaryTextField
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

@Composable
fun JobSearchScreen(navController: NavController, tokenProvider: TokenProvider){
    val coroutineScope = rememberCoroutineScope()
    var jobSearchResponse by remember { mutableStateOf<JobSearchResponse?>(null) }

    var searchText by remember { mutableStateOf("") }
    val filteredJobs = remember(jobSearchResponse, searchText){
        jobSearchResponse?.jobs?.filter { job ->
            job.title.contains(searchText, ignoreCase = true) ||
                    job.skills.any { skill -> skill.title.contains(searchText, ignoreCase = true)}
        } ?: emptyList()
    }
    val context = LocalContext.current


    LaunchedEffect(key1 = Unit){
        coroutineScope.launch{
            val jobSearchService = JobSearchService()
            jobSearchResponse = jobSearchService.fetchJobsForCurrentUserAsync(tokenProvider)
            Log.d("JobSearchScreen", jobSearchResponse.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        PrimaryTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = "Pretraži",
            modifier = Modifier.fillMaxWidth(),
            isError = false
        )
        if(jobSearchResponse != null && !jobSearchResponse!!.success){
            Text(text = jobSearchResponse!!.message!!)
        }else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(filteredJobs.size) { index ->
                    val job = filteredJobs[index]
                    Log.d("JobSearchScreen poslovi", job.toString())
                    JobListItem(job = job){
                        Toast.makeText(context, "Otvoreno: " + job.title, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JobSearchScreenPreview() {
    val navController = rememberNavController()
    JobSearchScreen(navController, tokenProvider = object : TokenProvider { override fun getToken(): String? { return null } })
}