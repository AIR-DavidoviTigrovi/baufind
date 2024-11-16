package hr.foi.air.baufind.ui.screens.WorkerSearchScreen

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun WorkerSearchScreen(navController: NavController){
    val viewModel: WorkerSearchViewModel = viewModel()
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Worker Search Screen")
    }
}
@Preview(showBackground = true)
@Composable
fun WorkerSearchScreenPreview(){
    val navController = rememberNavController()
    WorkerSearchScreen(navController)
}

