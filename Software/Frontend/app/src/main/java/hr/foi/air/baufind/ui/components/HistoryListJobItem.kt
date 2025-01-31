package hr.foi.air.baufind.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hr.foi.air.baufind.R
import hr.foi.air.baufind.helpers.PictureHelper

@Composable
fun HistoryListJobItem(
    picture: String,
    name: String,
    date: String,
    onItemClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable { onItemClick() },
        //horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        val imageData = PictureHelper.decodeBase64ToByteArray(picture)
        val bitmap = imageData?.let { android.graphics.BitmapFactory.decodeByteArray(it, 0, it.size) }
        if(bitmap != null){
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Slika posla",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .padding(end = 16.dp)
            )
        }else{
            Image(
                painter = painterResource(id = R.drawable.image_icon),
                contentDescription = "Nema slike",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .padding(end = 16.dp)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth()
        ){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Naslov:", modifier = Modifier.weight(0.4f))
                Text(text = name, modifier = Modifier.weight(0.6f))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Datum završetka:", modifier = Modifier.weight(0.4f))
                Text(text = date, modifier = Modifier.weight(0.6f))
            }
        }
    }
}