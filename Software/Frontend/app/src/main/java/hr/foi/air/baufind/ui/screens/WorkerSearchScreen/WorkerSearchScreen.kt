package hr.foi.air.baufind.ui.screens.WorkerSearchScreen

import android.widget.Toast
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hr.foi.air.baufind.mock.WorkerSearchMock.WorkerMock
import hr.foi.air.baufind.ui.components.WorkerCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerSearchScreen(navController: NavController) {
    val viewModel: WorkerSearchViewModel = viewModel()

    //Logika za dropdown meni
    /// Preporučeno je za manji broj opcija koristiti chip
    val isExpandedL by viewModel.isExpandedL
    val isExpandedR by viewModel.isExpandedR
    val scrollState = rememberScrollState()
    val selectedItemL by viewModel.selectedItemL
    val selectedItemR by viewModel.selectedItemR
    val workers by viewModel.workers
    val context = LocalContext.current
    // Opcije za dropdown meni
    val optionsR = listOf("Ocjena ASC", "Ocjena DESC", "Broj poslova ASC", "Broj poslova DESC")
    val optionsL = listOf("Zagrebačka", "Krapinsko-zagorska", "Sisacko-moslavačka", "Karlovačka", "Varaždinska",
         "Bjelovarsko-bilogorska", "Primorsko-goranska", "Ličko-senjska",
        "Virovitičko-podravska", "Osječko-baranjska", "Šibensko-kninska", "Vukovarsko-srijemska",
        "Zadarska", "Međimurska", "Dubrovničko-neretvanska", "Istarska", "Požeško-slavonska",
        "Splitsko-dalmatinska", "Zagrebački grad"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Pronađite poziciju x")


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
                    label = { Text("Location", overflow = TextOverflow.Ellipsis, maxLines = 1) }
                )
                ExposedDropdownMenu(
                    expanded = isExpandedL,
                    onDismissRequest = { viewModel.isExpandedL.value = false }
                ) {
                    optionsL.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                viewModel.selectedItemL.value = option
                                viewModel.isExpandedL.value = false
                            }
                        )
                    }
                }
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
                    label = { Text("Sort by", overflow = TextOverflow.Ellipsis, maxLines = 1) }
                )
                ExposedDropdownMenu(
                    expanded = isExpandedR,
                    onDismissRequest = { viewModel.isExpandedL.value = false }
                ) {
                    optionsR.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                viewModel.selectedItemR.value = option
                                viewModel.isExpandedR.value = false
                            }
                        )
                    }
                }
            }
        }
        //Lista radnika i prikaz i callback za pritisak
        LazyColumn(
            modifier = Modifier.scrollable(state = scrollState, orientation = Orientation.Vertical),

        ) {
            items(workers){
                worker -> WorkerCard(worker){
                    //Funkcija se poziva na pritiskom na radnika,| treba je promijeniti u kasnijim fazama i prilikom promjene obrišite dio komentara nakon | znaka.
                   Toast.makeText(context, "Clicked on ${worker.firstName} ${worker.lastName}", Toast.LENGTH_SHORT).show()
               }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkerSearchScreenPreview() {
    val navController = rememberNavController()
    WorkerSearchScreen(navController)
}
