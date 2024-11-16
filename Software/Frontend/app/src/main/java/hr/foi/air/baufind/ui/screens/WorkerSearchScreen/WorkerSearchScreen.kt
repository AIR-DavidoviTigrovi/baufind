package hr.foi.air.baufind.ui.screens.WorkerSearchScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun WorkerSearchScreen(navController: NavController){
    val viewModel: WorkerSearchViewModel = viewModel()
}
@Preview
@Composable
fun WorkerSearchScreenPreview(){
    val navController = rememberNavController()
    WorkerSearchScreen(navController)
}

