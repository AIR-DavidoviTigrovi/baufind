package hr.foi.air.baufind.ui.screens.MyJobsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import hr.foi.air.baufind.R
import hr.foi.air.baufind.ui.components.MyJobListItem
import hr.foi.air.baufind.ws.network.TokenProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyJobsScreen(
    navController: NavController,
    tokenProvider: TokenProvider,
    viewModel: MyJobsViewModel
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val isLoading by viewModel.isLoading
    LaunchedEffect(currentBackStackEntry) {
        viewModel.clearData()
        viewModel.tokenProvider = tokenProvider
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = remember { SnackbarHostState() })
        },
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        "Moji poslovi",
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
                actions = {
                    IconButton(onClick = {
                        navController.navigate("wholeHistoryScreen")
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_history_24),
                            contentDescription = "Povijest",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate("myJobsNotificationScreen")
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
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
            if (isLoading) {
                Text(text = "Učitavam...")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    items(viewModel.myCreatedJobs.value.size) { index ->
                        if (index == 0) {
                            Text(
                                text = "Moji poslovi",
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(
                                        bottom = 8.dp
                                    ),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        val job = viewModel.myCreatedJobs.value[index]
                        MyJobListItem(job = job){
                            navController.navigate("jobRoom/${job.id}")
                        }
                    }
                    items(viewModel.jobsImOn.value.size) { index ->
                        if (index == 0) {
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(
                                        top = 20.dp
                                    ),
                                thickness = 2.dp
                            )
                            Text(
                                text = "Poslovi na kojima radim",
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(
                                        bottom = 8.dp
                                    ),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        val job = viewModel.jobsImOn.value[index]
                        MyJobListItem(job = job){
                            navController.navigate("jobRoom/${job.id}")
                        }
                    }
                }
            }
        }
    }
}