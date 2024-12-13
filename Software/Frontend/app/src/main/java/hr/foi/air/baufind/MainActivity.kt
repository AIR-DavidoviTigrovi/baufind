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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import hr.foi.air.baufind.core.map.MapProvider
import hr.foi.air.baufind.example_map.ExampleMapProvider
import hr.foi.air.baufind.navigation.BottomNavigationBar
import hr.foi.air.baufind.ui.screens.JobCreateScreen.JobAddSkillsScreen
import hr.foi.air.baufind.ui.screens.JobCreateScreen.JobDetailsScreen
import hr.foi.air.baufind.ui.screens.JobCreateScreen.JobPositionsLocationScreen
import hr.foi.air.baufind.ui.screens.JobCreateScreen.JobViewModel
import hr.foi.air.baufind.ui.screens.LoginScreen.LoginScreen
import hr.foi.air.baufind.ui.screens.UserProfileScreen.EditProfileScreen
import hr.foi.air.baufind.ui.screens.UserProfileScreen.UserProfileViewModel
import hr.foi.air.baufind.ui.screens.UserProfileScreen.ReviewsScreen
import hr.foi.air.baufind.ui.screens.UserProfileScreen.userProfileScreen
import hr.foi.air.baufind.ui.screens.WorkerSearchScreen.WorkerSearchScreen
import hr.foi.air.baufind.ui.theme.BaufindTheme
import hr.foi.air.baufind.ws.network.AppTokenProvider

class MainActivity : ComponentActivity() {
    private val mapProviders: List<MapProvider> = listOf(ExampleMapProvider()) // TODO: ubaciti module pomoću refleksije (a ne ručno)
    private val mapProvider = mapProviders.first() // TODO: da se može odabrati u postavkama

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val tokenProvider = AppTokenProvider(sharedPreferences)
        val jwtToken = sharedPreferences.getString("jwt_token", null)
        val userProfileViewModel = UserProfileViewModel(tokenProvider)
        val gson: Gson = Gson()
        setContent {
            val jobViewModel : JobViewModel = viewModel()
            val navController = rememberNavController()
            val currentRoute = navController
                .currentBackStackEntryAsState().value?.destination?.route

            BaufindTheme {
                Scaffold(
                    bottomBar = {
                        if (currentRoute in listOf("jobPositionsLocationScreen")) {
                            BottomNavigationBar(navController = navController)
                        }
                        if (currentRoute in listOf("jobAddSkillsScreen")) {
                            BottomNavigationBar(navController = navController)
                        }
                        if (currentRoute in listOf("jobDetailsScreen")) {
                            BottomNavigationBar(navController = navController)
                        }
                        if (currentRoute in listOf("myUserProfileScreen")) {
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
                        else startDestination = "login" // promijeniti kada se doda token refresh
                        NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            composable("login") { LoginScreen(navController, this@MainActivity, tokenProvider) }
                            composable("registration") { RegistrationScreen(navController, tokenProvider) }

                            composable("myUserProfileScreen") { userProfileScreen(navController,this@MainActivity, tokenProvider, userProfileViewModel) }
                            composable("editUserProfileScreen") { EditProfileScreen(navController, this@MainActivity, tokenProvider, userProfileViewModel) }
                            composable("reviewsScreen/{userId}") { backStackEntry ->
                                val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
                                ReviewsScreen(navController, userId, tokenProvider)
                            }


                            composable("workersSearchScreen/{position}",
                                arguments = listOf(navArgument("position") { type = NavType.StringType })
                                ) { backStackEntry ->
                                val position = backStackEntry.arguments?.getString("position")
                                val deserializedList = gson.fromJson(position, Array<Int>::class.java).toList()
                                WorkerSearchScreen(navController,tokenProvider,deserializedList)
                            }
                            composable("jobDetailsScreen") { JobDetailsScreen(navController, jobViewModel) }
                            composable("jobPositionsLocationScreen") { JobPositionsLocationScreen(navController, jobViewModel, tokenProvider, mapProvider) }
                            composable("jobAddSkillsScreen") { JobAddSkillsScreen(navController, jobViewModel, tokenProvider) }

                        }
                    }
                }
            }
        }

    }
}

