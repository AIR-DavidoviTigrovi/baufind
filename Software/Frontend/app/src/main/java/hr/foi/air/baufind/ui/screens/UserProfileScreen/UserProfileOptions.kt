package hr.foi.air.baufind.ui.screens.UserProfileScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UserProfileOptions(
    onLogout: () -> Unit,
    onEditProfile: () -> Unit,
    onDeleteAccount: () -> Unit,
    editSkills: () -> Unit
){
    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Button(onClick = onEditProfile) {
            Text(text = "Edit Profile")
        }
        Button(onClick = onDeleteAccount,
            colors = ButtonDefaults.buttonColors(Color.Red)) {
            Text(text = "Delete Account")
        }
        Button(onClick = onLogout,) {
            Text(text = "Logout", color = Color.White)
        }
        Button(onClick =  editSkills) {
            Text(text = "Edit skills")
        }
    }
}