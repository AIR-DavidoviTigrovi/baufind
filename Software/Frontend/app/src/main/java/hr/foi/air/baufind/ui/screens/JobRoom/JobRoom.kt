package hr.foi.air.baufind.ui.screens.JobRoom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hr.foi.air.baufind.service.JobRoomService.RoomOwnerState
import hr.foi.air.baufind.ui.components.PersonInRoomCard
import hr.foi.air.baufind.ui.components.RoleInJobCard
import hr.foi.air.baufind.ws.network.TokenProvider

@Composable
fun JobRoomScreen(navController: NavController,tokenProvider: TokenProvider,jobID: Int){
    val viewModel: JobRoomViewModel = viewModel()
    viewModel.tokenProvider.value = tokenProvider
    val context = LocalContext.current
    val scrollableState = rememberScrollState()
    LaunchedEffect(Unit) {
        viewModel.getAllSkills()
        viewModel.getJobRoom(jobID)
        viewModel.loadJobPeople(jobID)
        viewModel.determinateOwner(context)
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollableState),

    ) {
        if (viewModel.jobRoom.value.isNotEmpty()) {
            Text(text = viewModel.jobRoom.value[0].jobTitle!!)
            RoleInJobCard(
                viewModel.jobRoom.value[0].allowWorkerInvite,
                viewModel.roomSkills.value,
                navController = navController,
                peopleInRoom = viewModel.peopleInRoom,
                jobID
            )
            if(viewModel.roomOwnerState.value == RoomOwnerState.Employer){
                if(viewModel.jobRoom.value[0].JobStatus == "Zapocet"){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Button(
                            onClick = {  },
                            modifier = Modifier.wrapContentWidth()
                        ) {
                            Text("Završi posao")
                        }
                    }
                }else if( viewModel.jobRoom.value[0].JobStatus != "Završen" || viewModel.jobRoom.value[0].JobStatus != "Zapocet"){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Button(
                            onClick = {  },
                            modifier = Modifier.wrapContentWidth()
                        ) {
                            Text("Započni posao")
                        }
                    }
                }

            }

        } else {

            Text(text = "Loading...")
        }

    }
}
@Preview(showBackground = true)
@Composable
fun JobRoomScreenPreview() {
    JobRoomScreen(navController = NavController(LocalContext.current),tokenProvider = object : TokenProvider { override fun getToken(): String? { return null }},9)
}