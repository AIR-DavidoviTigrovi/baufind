package hr.foi.air.baufind.ui.screens.MyJobsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import hr.foi.air.baufind.ui.components.WorkerCardNotificationDetail
import hr.foi.air.baufind.ws.model.Worker
import hr.foi.air.baufind.ws.network.TokenProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyJobNotificationDetailScreen(
    navController: NavController,
    tokenProvider: TokenProvider,
    viewModel: MyJobsNotificationsViewModel,
    jobId : Int
){
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isLoading by viewModel.isLoading
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarColor = if (viewModel.success) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    SnackbarHost(
        hostState = SnackbarHostState(),
        modifier = Modifier.padding(16.dp),
        snackbar = { data ->
            Snackbar(
                snackbarData = data,
                containerColor = snackbarColor,
                contentColor = Color.White
            )
        }
    )


    LaunchedEffect(currentBackStackEntry) {
        viewModel.clearData()
        viewModel.tokenProvider = tokenProvider
    }

    LaunchedEffect(viewModel.snackbarMessage.value) {
        val message = viewModel.snackbarMessage.value
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        "Detalji",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Natrag",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isLoading) {
                } else {
                    val groupedNotifications = viewModel.getNotificationsGroupedBySkill(jobId)

                    groupedNotifications.forEach { (skill, notifications) ->

                        item {
                            Text(
                                text = "Uloga : $skill",
                                modifier = Modifier.padding(8.dp),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(notifications) { notification ->
                            WorkerCardNotificationDetail(
                                Worker(
                                    notification.workerId,
                                    notification.name,
                                    notification.address,
                                    notification.completedJobsCount,
                                    "",
                                    notification.rating
                                ),
                                onItemClick = {
                                    navController.navigate("reviewsScreen/${notification.workerId}")
                                },
                                onConfirmClick = {
                                    viewModel.confirmWorker(
                                        tokenProvider,
                                        notification.workerId,
                                        notification.jobId,
                                        notification.skillId,
                                        4
                                    )
                                    viewModel.clearData()
                                    navController.popBackStack()
                                },
                                onDenyClick = {
                                    viewModel.confirmWorker(
                                        tokenProvider,
                                        notification.workerId,
                                        notification.jobId,
                                        notification.skillId,
                                        5
                                    )
                                    viewModel.clearData()
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}