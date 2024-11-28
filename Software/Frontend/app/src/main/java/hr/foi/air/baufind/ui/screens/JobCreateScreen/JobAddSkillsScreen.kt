package hr.foi.air.baufind.ui.screens.JobCreateScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

//ekran za dodavanje uloga koje su potrebne za posao
@Composable
fun JobAddSkillsScreen(navController: NavController){

}

@Preview(showBackground = true)
@Composable
fun JobAddSkillsScreenPreview() {
    val navController = rememberNavController()
    JobAddSkillsScreen(navController)
}