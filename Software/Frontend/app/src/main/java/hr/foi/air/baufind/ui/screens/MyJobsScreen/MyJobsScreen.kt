package hr.foi.air.baufind.ui.screens.MyJobsScreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hr.foi.air.baufind.ws.network.TokenProvider

@Composable
fun MyJobsScreen(
    navController: NavController,
    tokenProvider: TokenProvider
) {
    Text(text = "Zaslon mojih poslova!");
}