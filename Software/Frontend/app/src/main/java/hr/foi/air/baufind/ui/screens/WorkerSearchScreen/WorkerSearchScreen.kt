package hr.foi.air.baufind.ui.screens.WorkerSearchScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerSearchScreen(navController: NavController) {
    val viewModel: WorkerSearchViewModel = viewModel()


    val isExpandedL by viewModel.isExpandedL
    val isExpandedR by viewModel.isExpandedR

    val selectedItemL by viewModel.selectedItemL
    val selectedItemR by viewModel.selectedItemR


    val optionsR = listOf("Ocjena ASC", "Ocjena DESC", "Broj poslova ASC", "Broj poslova DESC")
    val optionsL = listOf("Zagrebačka", "Krapinsko-zagorska", "Sisacko-moslavačka", "Karlovačka", "Varaždinska",
         "Bjelovarsko-bilogorska", "Primorsko-goranska", "Ličko-senjska",
        "Virovitičko-podravska", "Osječko-baranjska", "Šibensko-kninska", "Vukovarsko-srijemska",
        "Zadarska", "Međimurska", "Dubrovničko-neretvanska", "Istarska", "Požeško-slavonska",
        "Splitsko-dalmatinska", "Zagrebački grad"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Worker Search Screen")

        // Left Dropdown
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            ExposedDropdownMenuBox(
                expanded = isExpandedL,
                onExpandedChange = { viewModel.isExpandedL.value = it },
                modifier = Modifier.fillMaxWidth(0.3f)
            ) {
                TextField(
                    value = selectedItemL,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedL) },
                    modifier = Modifier.menuAnchor()
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
            ExposedDropdownMenuBox(
                expanded = isExpandedR,
                onExpandedChange = { viewModel.isExpandedR.value = it },
                modifier = Modifier.fillMaxWidth(0.4f)
            ) {
                TextField(
                    value = selectedItemR,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedL) },
                    modifier = Modifier.menuAnchor()
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
        
    }
}

@Preview(showBackground = true)
@Composable
fun WorkerSearchScreenPreview() {
    val navController = rememberNavController()
    WorkerSearchScreen(navController)
}
