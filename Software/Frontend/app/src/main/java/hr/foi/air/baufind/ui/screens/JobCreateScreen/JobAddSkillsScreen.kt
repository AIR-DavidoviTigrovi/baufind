package hr.foi.air.baufind.ui.screens.JobCreateScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hr.foi.air.baufind.ui.components.PrimaryTextField
import hr.foi.air.baufind.ui.components.SkillListConfirm
import kotlin.text.contains

@Composable
fun JobAddSkillsScreen(navController: NavController, jobViewModel: JobViewModel){
    var searchText by remember { mutableStateOf("") }
    val myStrings = listOf("Pozicija 1", "Pozicija 2", "Pozicija 3")
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PrimaryTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = "Pretraži",
            modifier = Modifier.fillMaxWidth(),
            isError = false
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(myStrings.filter { it.contains(searchText, ignoreCase = true) }) { text ->
                SkillListConfirm(
                    text = text,
                    onConfirmClick = {
                        if (!jobViewModel.jobPositions.any { it.name == text }){
                            jobViewModel.jobPositions.add(JobPosition(text, mutableIntStateOf(1)))
                            navController.popBackStack()
                        }
                        Toast.makeText(context, "Pozicija već postoji", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JobAddSkillsScreenPreview() {
    val navController = rememberNavController()
    JobAddSkillsScreen(navController, JobViewModel())
}