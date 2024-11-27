package hr.foi.air.baufind.ui.screens.UserProfileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserProfileHeader(name: String, address:String, profilePicture: ByteArray?){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        ){
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = name, color = MaterialTheme.colorScheme.primary ,style = TextStyle(fontSize = 20.sp), textAlign = TextAlign.Center, fontWeight = Bold, fontSize = 22.sp, modifier = Modifier.fillMaxWidth())
            Text(text = address, color = MaterialTheme.colorScheme.onSurface ,style = TextStyle(fontSize = 14.sp, color = Color.Gray), textAlign = TextAlign.Center, fontSize = 16.sp, modifier = Modifier.fillMaxWidth())
        }
    }
}