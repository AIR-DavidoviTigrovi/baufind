package hr.foi.air.baufind.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun PictureItem(imageUri: Uri) {
    AsyncImage(
        model = imageUri,
        contentDescription = null,
        modifier = Modifier.size(100.dp)
    )
}