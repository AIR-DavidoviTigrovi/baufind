package hr.foi.air.baufind.ui.screens.ReviewsScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter

@Composable
fun ReviewEmployerScreen(
    jobId: Int,
    reviewViewModel: ReviewViewModel,
    onReviewSubmitted: () -> Unit,
    context: Context
) {
    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                if (data.clipData != null) {
                    val clipData = data.clipData!!
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        if (!reviewViewModel.selectedImages.contains(uri)) {
                            reviewViewModel.selectedImages.add(uri)
                        }
                    }
                } else if (data.data != null) {
                    val singleUri = data.data!!
                    if (!reviewViewModel.selectedImages.contains(singleUri)) {
                        reviewViewModel.selectedImages.add(singleUri)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 24.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Recenziraj poslodavca",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        RatingStars(
            currentRating = rating,
            onRatingSelected = { newRating ->
                rating = newRating
                if (showError) showError = false
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = comment,
            onValueChange = {
                comment = it
                if (showError) showError = false
            },
            label = { Text("Komentar", color = MaterialTheme.colorScheme.onBackground) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (showError) {
            Text(
                text = "Molimo odaberite ocjenu i unesite komentar (min. 35 znakova).",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
                launcher.launch(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Učitaj fotografije")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (reviewViewModel.selectedImages.isNotEmpty()) {
            Text(
                text = "Odabrane fotografije:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            LazyRow {
                items(reviewViewModel.selectedImages) { uri ->
                    PictureItem(
                        imageUri = uri,
                        onRemove = {
                            reviewViewModel.selectedImages.remove(uri)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (rating == 0 || comment.length < 35) {
                    showError = true
                } else {
                    reviewViewModel.submitEmployerReview(jobId, rating, comment, context)
                    rating = 0
                    comment = ""
                    reviewViewModel.selectedImages.clear()
                    onReviewSubmitted()
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Pošalji recenziju")
        }
    }
}

@Composable
fun PictureItem(
    imageUri: Uri,
    onRemove: () -> Unit
) {
    Box(modifier = Modifier.padding(4.dp)) {
        Image(
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp),
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Remove image",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
                .clickable { onRemove() }
        )
    }
}


@Composable
fun RatingStars(
    currentRating: Int,
    totalStars: Int = 5,
    onRatingSelected: (Int) -> Unit
) {
    Row {
        for (starIndex in 1..totalStars) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star $starIndex",
                tint = if (starIndex <= currentRating) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .clickable { onRatingSelected(starIndex) }
            )
        }
    }
}