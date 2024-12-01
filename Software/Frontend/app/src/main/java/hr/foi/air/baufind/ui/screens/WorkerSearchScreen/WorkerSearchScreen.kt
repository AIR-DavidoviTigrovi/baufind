package hr.foi.air.baufind.ui.screens.WorkerSearchScreen

import android.text.style.ClickableSpan
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hr.foi.air.baufind.ui.components.WorkerCard
import hr.foi.air.baufind.ws.model.Worker
import hr.foi.air.baufind.ws.network.TokenProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerSearchScreen(navController: NavController,tokenProvider: TokenProvider,position: String) {
    val viewModel: WorkerSearchViewModel = viewModel()
    viewModel.tokenProvider.value = tokenProvider
    //Logika za dropdown meni
    /// Preporučeno je za manji broj opcija koristiti chip
    val isExpandedL by viewModel.isExpandedL
    val isExpandedR by viewModel.isExpandedR
    val scrollState = rememberScrollState()
    val selectedItemL by viewModel.selectedItemL
    val selectedItemR by viewModel.selectedItemR
    val context = LocalContext.current
    // Opcije za dropdown meni
    val optionsR = viewModel.optionsR
    val optionsL = viewModel.optionsL
    val workers by viewModel.filteredWorkers
    
    LaunchedEffect(Unit) {
        viewModel.loadWorkers()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Pronađite poziciju ${position}")


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
                        viewModel.loadWorkers()
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
        if (viewModel.workers.value == emptyList<Worker>() || viewModel.filteredWorkers.value == emptyList<Worker>()){
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

                worker -> WorkerCard(worker){
                    //Funkcija se poziva na pritiskom na radnika,| treba je promijeniti u kasnijim fazama i prilikom promjene obrišite dio komentara nakon | znaka.
                   Toast.makeText(context, "Clicked on ${worker.name}", Toast.LENGTH_SHORT).show()
               }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun WorkerSearchScreenPreview() {
    val navController = rememberNavController()
    WorkerSearchScreen(navController,tokenProvider = object : TokenProvider { override fun getToken(): String? { return null }},"")
}
