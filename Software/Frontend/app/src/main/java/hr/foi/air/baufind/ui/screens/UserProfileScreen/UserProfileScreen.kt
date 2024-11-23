package hr.foi.air.baufind.ui.screens.UserProfileScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.foi.air.baufind.ui.components.Skill

@Composable
fun userProfileScreen(name: String,phone: String, address:String, email: String, profilePicture: ByteArray?){
    Scaffold(
        topBar = {

        }
    ){
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
        Column(modifier = Modifier.padding(it))
        {

            UserProfileHeader(name, phone, address, email, profilePicture)
            Spacer(modifier = Modifier.width(22.dp))
            UserSkillSection(skills)
            Spacer(modifier = Modifier.width(22.dp))
            UserProfileOptions(onLogout = {}, onEditProfile = {}, onDeleteAccount = {}, editSkills = {})
        }
    }
}
@Preview(showBackground = true)
@Composable
fun userProfileScreenPreview(){
    userProfileScreen("Ivo Android","063188196" , "Kralja Petra Krešimira IV 42", email = "iandric@gmail.com",  null)

}