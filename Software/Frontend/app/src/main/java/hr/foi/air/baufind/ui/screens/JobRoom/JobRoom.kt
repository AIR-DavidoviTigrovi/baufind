package hr.foi.air.baufind.ui.screens.JobRoom

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hr.foi.air.baufind.ws.network.TokenProvider

@Composable
fun JobRoomScreen(navController: NavController,tokenProvider: TokenProvider,jobID: Int){
    val viewModel: JobRoomViewModel = viewModel()
    viewModel.tokenProvider.value = tokenProvider

    LaunchedEffect(Unit) {
        viewModel.getJobRoom(jobID)
    }
    Column() {
       
    }
}
@Preview(showBackground = true)
@Composable
fun JobRoomScreenPreview() {
    JobRoomScreen(navController = NavController(LocalContext.current),tokenProvider = object : TokenProvider { override fun getToken(): String? { return null }},33)
}