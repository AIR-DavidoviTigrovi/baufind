package hr.foi.air.baufind.ui.screens.UserProfileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserProfileHeader(name: String, phone:String, address:String, email: String, profilePicture: ByteArray?){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        ){

        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = name, style = TextStyle(fontSize = 20.sp))
            Text(text = phone, style = TextStyle(fontSize = 14.sp, color = Color.Gray))
            Text(text = address, style = TextStyle(fontSize = 14.sp, color = Color.Gray))
            Text(text = email, style = TextStyle(fontSize = 14.sp, color = Color.Gray))
        }
    }

}