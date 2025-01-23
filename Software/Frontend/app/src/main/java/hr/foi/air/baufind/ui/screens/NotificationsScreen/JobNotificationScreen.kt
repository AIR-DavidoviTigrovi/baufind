package hr.foi.air.baufind.ui.screens.NotificationsScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import hr.foi.air.baufind.ws.network.TokenProvider

@Composable
fun JobNotificationScreen(
    navController: NavController,
    tokenProvider: TokenProvider,
    viewModel: JobNotificationViewModel
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isLoading by viewModel.isLoading
    val jobs = viewModel.jobs
    val reviewList = viewModel.reviewNotifications

    LaunchedEffect(currentBackStackEntry) {
        viewModel.tokenProvider = tokenProvider
    }

    val invitedJobs = jobs.filter { it?.working_status_id == 3 }
    val acceptedJobs = jobs.filter { it?.working_status_id == 4 }
    val reviewJobs = jobs.filter { it?.job_status_id == 3 }

    if (isLoading) {
        LoadingIndicator()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(invitedJobs.filterNotNull()) { index, job ->
                JobNotificationCard(job,navController,tokenProvider)
            }
            itemsIndexed(acceptedJobs.filterNotNull()) { index, job ->
                JobNotificationCard(job,navController,tokenProvider)
            }
            itemsIndexed(reviewJobs.filterNotNull()) { index, job ->
                JobNotificationCard(job,navController,tokenProvider)
            }
            itemsIndexed(reviewList) { index, review ->
                ReviewNotificationCard(review, navController)
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

