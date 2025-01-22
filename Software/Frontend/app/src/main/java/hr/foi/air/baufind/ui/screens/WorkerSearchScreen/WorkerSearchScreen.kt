package hr.foi.air.baufind.ui.screens.WorkerSearchScreen

import android.text.style.ClickableSpan
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import hr.foi.air.baufind.ui.components.WorkerCard
import hr.foi.air.baufind.ui.screens.JobCreateScreen.JobViewModel
import hr.foi.air.baufind.ws.model.Worker
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerSearchScreen(
    navController: NavController,
    tokenProvider: TokenProvider,
    skills: MutableList<Int>,
    listOfIDs: MutableList<Int>,
    jobId : Int,
    viewModel: WorkerSearchViewModel,
    jobViewModel : JobViewModel
    ) {
    viewModel.tokenProvider.value = tokenProvider
    //Logika za dropdown meni
    /// Preporučeno je za manji broj opcija koristiti chip6
    viewModel.listofIDs.value = listOfIDs
    var skill by viewModel.skillStrings
    var isLoading by remember { mutableStateOf(true) }
    val isExpandedL by viewModel.isExpandedL
    val isExpandedR by viewModel.isExpandedR
    val scrollState = rememberScrollState()
    val selectedItemL by viewModel.selectedItemL
    val selectedItemR by viewModel.selectedItemR
    viewModel.jobID.value = jobId
    val context = LocalContext.current
    // Opcije za dropdown meni
    val optionsR = viewModel.optionsR
    val optionsL = viewModel.optionsL
    val workers by viewModel.filteredWorkers
    val coroutine = rememberCoroutineScope()

    LaunchedEffect(viewModel.skillsId.value) {
        isLoading = true

        if(viewModel.isEmptyList && viewModel.skillsId.value.isEmpty()){
            viewModel.clearData()
            jobViewModel.clearData()
            navController.popBackStack()
        }

        if (viewModel.skillsId.value.isEmpty() && !viewModel.isEmptyList) {
            viewModel.skillsId.value = skills
        }

        viewModel.getAllSkills()
        viewModel.loadWorkers()
        isLoading = false
    }



    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Pronađite pozicije ${skill}")


        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            //Lijevi dropdown meni
            ExposedDropdownMenuBox(
                expanded = isExpandedL,
                onExpandedChange = { viewModel.isExpandedL.value = it },
                modifier = Modifier.width(128.dp)
            ) {
                TextField(
                    value = selectedItemL,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedL) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    label = { Text("Lokacija", overflow = TextOverflow.Ellipsis, maxLines = 1) }
                )
                ExposedDropdownMenu(
                    expanded = isExpandedL,
                    onDismissRequest = { viewModel.isExpandedL.value = false }
                ) {
                    optionsL.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                viewModel.updateFilteredWorkersL(option)
                            }
                        )
                    }
                }
            }
            if(selectedItemL != "" || selectedItemR != "") {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close icon",
                    tint = Color.Black,
                    modifier = Modifier.padding(12.dp, 0.dp).clickable {
                        viewModel.updateFilteredWorkersR("")
                        viewModel.updateFilteredWorkersL("")
                        coroutine.launch {
                            isLoading = true
                            viewModel.loadWorkers()
                            isLoading = false

                        }
                    }

                )
            }
            //Desni dropdown meni
            ExposedDropdownMenuBox(
                expanded = isExpandedR,
                onExpandedChange = { viewModel.isExpandedR.value = it },
                modifier = Modifier.width(128.dp)
            ) {
                TextField(
                    value = selectedItemR,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedL) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    label = { Text("Sortiraj", overflow = TextOverflow.Ellipsis, maxLines = 1) }
                )
                ExposedDropdownMenu(
                    expanded = isExpandedR,
                    onDismissRequest = { viewModel.isExpandedL.value = false }
                ) {
                    optionsR.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                               viewModel.updateFilteredWorkersR(option)
                            }
                        )
                    }
                }
            }
        }
        if(isLoading){
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth().fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
        if (!isLoading && viewModel.workers.value == emptyList<Worker>() || viewModel.filteredWorkers.value == emptyList<Worker>()){
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth().fillMaxHeight()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "No Workers",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Nema radnika koje pretražujete",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        //Lista radnika i prikaz i callback za pritisak
        LazyColumn(
            modifier = Modifier.scrollable(state = scrollState, orientation = Orientation.Vertical),

        ) {

            items(workers){

                worker ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                        initialOffsetY = { it }
                    ),
                ) {

                    WorkerCard(worker){
                        navController.navigate("workersProfileScreen/${jobId}/${worker.id}/${viewModel.skillsId.value.first()}")
                    }
                }
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearData()
        }
    }

}


