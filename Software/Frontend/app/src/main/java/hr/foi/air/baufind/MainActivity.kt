package hr.foi.air.baufind

import RegistrationScreen
import android.content.Context
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
import hr.foi.air.baufind.ui.screens.LoginScreen.LoginScreen
import hr.foi.air.baufind.ui.screens.WorkerSearchScreen.WorkerSearchScreen
import hr.foi.air.baufind.ui.theme.BaufindTheme
import hr.foi.air.baufind.ws.network.AppTokenProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val tokenProvider = AppTokenProvider(sharedPreferences)
        val jwtToken = sharedPreferences.getString("jwt_token", null)

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
                        val startDestination : String
                        if (jwtToken == null) startDestination ="login"
                        else startDestination = "workersSearchScreen"
                        NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            composable("login") { LoginScreen(navController, this@MainActivity, tokenProvider) }
                            composable("registration") { RegistrationScreen(navController, tokenProvider) }
                            composable("workersSearchScreen") { WorkerSearchScreen(navController,tokenProvider,"") }
                        }
                    }
                }
            }
        }

    }
}

