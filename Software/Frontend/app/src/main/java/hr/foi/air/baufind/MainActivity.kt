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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import hr.foi.air.baufind.core.map.MapProvider
import hr.foi.air.baufind.example_map.ExampleMapProvider
import hr.foi.air.baufind.google_map.GoogleMapProvider
import hr.foi.air.baufind.helpers.MapHelper
import hr.foi.air.baufind.navigation.BottomNavigationBar
import hr.foi.air.baufind.open_street_map.OpenStreetMapProvider
import hr.foi.air.baufind.ui.screens.JobCreateScreen.JobAddSkillsScreen
import hr.foi.air.baufind.ui.screens.JobCreateScreen.JobDetailsScreen
import hr.foi.air.baufind.ui.screens.JobCreateScreen.JobPositionsLocationScreen
import hr.foi.air.baufind.ui.screens.JobCreateScreen.JobViewModel
import hr.foi.air.baufind.ui.screens.JobRoom.JobRoomScreen
import hr.foi.air.baufind.ui.screens.JobSearchScreen.JobSearchDetailsScreen
import hr.foi.air.baufind.ui.screens.JobSearchScreen.JobSearchDetailsViewModel
import hr.foi.air.baufind.ui.screens.JobSearchScreen.JobSearchScreen
import hr.foi.air.baufind.ui.screens.JobSearchScreen.JobSearchViewModel
import hr.foi.air.baufind.ui.screens.LoginScreen.LoginScreen
import hr.foi.air.baufind.ui.screens.Settings.SettingsScreen
import hr.foi.air.baufind.ui.screens.UserProfileScreen.EditProfileScreen
import hr.foi.air.baufind.ui.screens.UserProfileScreen.ReviewsScreen
import hr.foi.air.baufind.ui.screens.UserProfileScreen.UserProfileViewModel
import hr.foi.air.baufind.ui.screens.UserProfileScreen.userProfileScreen
import hr.foi.air.baufind.ui.screens.WorkerSearchScreen.WorkerProfileScreen
import hr.foi.air.baufind.ui.screens.WorkerSearchScreen.WorkerSearchScreen
import hr.foi.air.baufind.ui.theme.BaufindTheme
import hr.foi.air.baufind.ws.network.AppTokenProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val tokenProvider = AppTokenProvider(sharedPreferences)
        val jwtToken = sharedPreferences.getString("jwt_token", null)
        val userProfileViewModel = UserProfileViewModel(tokenProvider)
        val gson = Gson()
        val jobViewModel = JobViewModel()
        val jobSearchViewModel = JobSearchViewModel()
        val jobSearchDetailsViewModel = JobSearchDetailsViewModel()
        setContent {
            val navController = rememberNavController()
            val currentRoute = navController
                .currentBackStackEntryAsState().value?.destination?.route

            BaufindTheme {
                Scaffold(
                    bottomBar = {
                        if (currentRoute?.startsWith("userProfileScreen") == true ||
                            currentRoute in listOf(
                                "jobPositionsLocationScreen",
                                "jobAddSkillsScreen",
                                "jobDetailsScreen",
                                "jobSearchScreen",
                                "jobSearchDetailsScreen",
                                "settingsScreen"
                            )
                        ) {
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

                            composable(
                                "userProfileScreen?userId={userId}",
                                arguments = listOf(
                                    navArgument("userId") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                        nullable = false
                                    }
                                )
                            ) { backStackEntry ->
                                val userId = backStackEntry.arguments?.getInt("userId") ?: -1

                                userProfileScreen(
                                    navController = navController,
                                    context = this@MainActivity,
                                    tokenProvider = tokenProvider,
                                    userProfileViewModel = userProfileViewModel,
                                    userId = if (userId == -1) null else userId
                                )

                            }


                            composable("editUserProfileScreen") { EditProfileScreen(navController, this@MainActivity, tokenProvider, userProfileViewModel) }
                            composable("reviewsScreen/{userId}") { backStackEntry ->
                                val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
                                ReviewsScreen(navController, userId, tokenProvider)
                            }

                            var deserializedList = mutableListOf<Int>()
                            composable("workersSearchScreen/{position}/{jobId}",
                                arguments = listOf(navArgument("position") { type = NavType.StringType },
                                    navArgument("jobId") { type = NavType.IntType }
                                )
                                ) { backStackEntry ->
                                val position = backStackEntry.arguments?.getString("position")
                                val jobId = backStackEntry.arguments?.getInt("jobId")

                                deserializedList = gson.fromJson(position, Array<Int>::class.java).toMutableList()
                                WorkerSearchScreen(navController,tokenProvider,deserializedList, jobId!!)
                            }
                            composable(
                                "workersProfileScreen/{jobId}/{workerId}/{skillId}",
                                arguments = listOf(
                                    navArgument("jobId") { type = NavType.IntType },
                                    navArgument("workerId") { type = NavType.IntType },
                                    navArgument("skillId") { type = NavType.IntType }
                                )
                            ) { backStackEntry ->
                                val jobId = backStackEntry.arguments?.getInt("jobId") ?: 0
                                val workerId = backStackEntry.arguments?.getInt("workerId") ?: 0
                                val skillId = backStackEntry.arguments?.getInt("skillId") ?: 0


                                WorkerProfileScreen(
                                    navController = navController,
                                    context = this@MainActivity,
                                    tokenProvider = tokenProvider,
                                    id = workerId,
                                    skills = deserializedList,
                                    jobId = jobId,
                                    skillId = skillId
                                )
                            }


                            composable("jobRoom/{jobID}",
                                arguments = listOf(navArgument("jobID") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val position = backStackEntry.arguments?.getInt("jobID")

                                JobRoomScreen(navController,tokenProvider,position!!)

                            }
                            composable("jobDetailsScreen") { JobDetailsScreen(navController, jobViewModel) }
                            composable("jobPositionsLocationScreen") { JobPositionsLocationScreen(navController, jobViewModel, tokenProvider) }
                            composable("jobAddSkillsScreen") { JobAddSkillsScreen(navController, jobViewModel, tokenProvider) }

                            composable("jobSearchScreen") { JobSearchScreen(navController, tokenProvider, jobSearchViewModel, jobSearchDetailsViewModel) }
                            composable("jobSearchDetailsScreen") { JobSearchDetailsScreen(navController, tokenProvider, jobSearchDetailsViewModel) }
                            composable("settingsScreen") { SettingsScreen(navController) }

                        }
                    }
                }
            }
        }

    }
}

