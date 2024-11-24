package hr.foi.air.baufind.ui.screens.UserProfileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
fun userProfileReview(){
    /*
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp)
        .clip(CircleShape)
        .background(Color.LightGray)
        .padding(horizontal = 12.dp, vertical = 6.dp)
        .clickable { onViewReviews() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5){ index ->
            val starColor = when {
                index < averageRating.toInt() -> Color.Yellow
                index < averageRating -> Color.Yellow.copy(alpha = 0.5f)
                else -> Color.Gray
            }
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(starColor)
                    .padding(2.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "(${averageRating}) Reviews",
            style = TextStyle(fontSize = 14.sp, color = Color.DarkGray)
        )
    }*/
}