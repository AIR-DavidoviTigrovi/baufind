package hr.foi.air.baufind.ui.screens.UserProfileScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun userProfileScreen(name: String,phone: String, address:String, email: String, profilePicture: ByteArray?){
    Scaffold(
        topBar = {

        }
    ){
        Column(modifier = Modifier.padding(it))
        {
            UserProfileHeader(name, phone, address, email, profilePicture)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun userProfileScreenPreview(){
    userProfileScreen("Ivo Android","063188196" , "Kralja Petra Krešimira IV 42", email = "iandric@gmail.com",  null)

}