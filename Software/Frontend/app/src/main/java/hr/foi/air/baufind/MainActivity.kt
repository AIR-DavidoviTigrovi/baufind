package hr.foi.air.baufind

import RegistrationScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hr.foi.air.baufind.navigation.BottomNavigationBar
import hr.foi.air.baufind.ui.screens.JobCreateScreen.JobDetailsScreen
import hr.foi.air.baufind.ui.screens.LoginScreen.LoginScreen
import hr.foi.air.baufind.ui.screens.WorkerSearchScreen.WorkerSearchScreen
import hr.foi.air.baufind.ui.theme.BaufindTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val currentRoute = navController
                .currentBackStackEntryAsState().value?.destination?.route

            BaufindTheme {
                Scaffold(
                    bottomBar = {
                        if (currentRoute in listOf("workersSearchScreen")) {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "jobDetailsScreen"
                        ) {
                            composable("login") { LoginScreen(navController) }
                            composable("registration") { RegistrationScreen(navController) }
                            composable("workersSearchScreen") { WorkerSearchScreen(navController) }
                            composable("jobDetailsScreen") { JobDetailsScreen(navController) }
                            //composable("jobPositionsLocationScreen") { JobPositionsLocationScreen(navController) }
                            //composable("jobAddSkillsScreen") { JobAddSkillsScreen(navController) }
                        }
                    }
                }
            }
        }

    }
}

