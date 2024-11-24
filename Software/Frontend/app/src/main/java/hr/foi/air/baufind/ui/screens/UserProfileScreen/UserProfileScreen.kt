@file:OptIn(ExperimentalMaterial3Api::class)

package hr.foi.air.baufind.ui.screens.UserProfileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.foi.air.baufind.ui.components.Skill

@Composable
fun userProfileScreen(name: String,phone: String, address:String, email: String, profilePicture: ByteArray?){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(modifier = Modifier.fillMaxWidth(),
                title = { Text("Profile", fontWeight = FontWeight.Bold)  },
                navigationIcon = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ){ paddingValues ->
        val skills = listOf(
        Skill("Skill 1"),
        Skill("Skill 2"),
        Skill("Skill 3"),
        Skill("Skill 4"),
        Skill("Skill 5"),
        Skill("Skill 6"),
        Skill("Skill 7"),
        Skill("Skill 8")
    )
        Column(modifier = Modifier.padding(paddingValues).fillMaxWidth().background(Color.White))
        {
            UserProfileHeader(name, address, profilePicture)
            UserProfileContactInformation(address, phone, email)
            UserSkillSection(skills)
            Spacer(modifier = Modifier.width(22.dp))
        }
    }
}
@Preview(showBackground = true)
@Composable
fun userProfileScreenPreview(){
    userProfileScreen("Ivo Android","063188196" , "Kralja Petra Krešimira IV 42", email = "iandric@gmail.com",  null)

}