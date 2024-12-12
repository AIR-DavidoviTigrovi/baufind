package hr.foi.air.baufind.ui.screens.JobSearchScreen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import hr.foi.air.baufind.service.JobSearchService.JobSearchService
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

@Composable
fun JobSearchScreen(navController: NavController, tokenProvider: TokenProvider){
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit){
        coroutineScope.launch{
            val jobSearchService = JobSearchService()
            val response = jobSearchService.fetchJobsForCurrentUserAsync(tokenProvider)
            Log.d("JobSearchScreen", response.toString())
        }
    }
}