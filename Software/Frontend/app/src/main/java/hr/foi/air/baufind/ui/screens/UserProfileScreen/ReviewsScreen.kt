package hr.foi.air.baufind.ui.screens.UserProfileScreen

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import hr.foi.air.baufind.service.ReviewService.ReviewService
import hr.foi.air.baufind.ws.model.Review
import hr.foi.air.baufind.ws.network.TokenProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(navController: NavController, userId: Int, tokenProvider: TokenProvider) {
    val reviewService = ReviewService(tokenProvider)
    var workerReviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var employerReviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        try {
            val response = reviewService.getUserReviews(userId)
            if (response != null) {
                workerReviews = response.workerReviews ?: emptyList()
                employerReviews = response.employerReviews ?: emptyList()
                error = response.error
            }
        } catch (e: Exception) {
            e.printStackTrace()
            error = "Failed to load reviews."
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        "Reviews",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (error != null) {
                Text(
                    text = error ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                ReviewsTabContent(
                    workerReviews = workerReviews,
                    employerReviews = employerReviews
                )
            }
        }
    }
}
@Composable
fun ReviewsTabContent(workerReviews: List<Review>, employerReviews: List<Review>) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Worker", "Employer")

    Column {
        ConnectedTabLayout(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )

        when (selectedTabIndex) {
            0 -> ReviewsList(reviews = workerReviews)
            1 -> ReviewsList(reviews = employerReviews)
        }
    }
}

@Composable
fun ConnectedTabLayout(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            tabs.forEachIndexed { index, title ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            if (index == selectedTabIndex) MaterialTheme.colorScheme.background
                            else MaterialTheme.colorScheme.primaryContainer
                        )
                        .clickable { onTabSelected(index) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (index == selectedTabIndex) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewsList(reviews: List<Review>) {
    LazyColumn {
        items(reviews) { review ->
            ReviewItem(review = review)
        }
    }
}
@Composable
fun StarRating(rating: Double) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = when {
                    index < rating.toInt() -> MaterialTheme.colorScheme.secondary // Full star
                    index < rating -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f) // Half star
                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f) // no star
                },
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Composable
fun ShowImageZoomDialog(image: ImageBitmap, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Image(
                bitmap = image,
                contentDescription = "Zoomed Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    var isDialogVisible by remember { mutableStateOf(false) }
    var selectedImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var currentPage by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Profile Image
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), CircleShape)
        ) {
            val imageData = review.reviewerImage?.let { decodeBase64ToByteArray(it) }
            val bitmap = imageData?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Reviewer Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = "N/A",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Review Content
        Column(modifier = Modifier.weight(1f)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Reviewer Name
                Text(
                    text = review.reviewerName,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                // Date Placeholder
                Text(
                    text = "Date Placeholder",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Star Rating
            StarRating(rating = review.rating.toDouble())

            Spacer(modifier = Modifier.height(8.dp))

            // Comment
            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Review Pictures
            if (review.pictures.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (review.pictures.size > 5) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous",
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .clickable {
                                    currentPage = (currentPage - 1).coerceAtLeast(0)
                                },
                            tint = MaterialTheme.colorScheme.onSurface
                        )

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next",
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clickable {
                                    currentPage = (currentPage + 1).coerceAtMost((review.pictures.size - 1) / 5)
                                },
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                    ) {
                        val startIndex = currentPage * 5
                        val endIndex = (startIndex + 5).coerceAtMost(review.pictures.size)

                        items(review.pictures.subList(startIndex, endIndex)) { picture ->
                            val decodedBytes = decodeBase64ToByteArray(picture.picture)

                            val imageBitmap = decodedBytes?.let {
                                BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
                            }
                            if (imageBitmap != null) {
                                Image(
                                    bitmap = imageBitmap,
                                    contentDescription = "Review Picture",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(end = 8.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                        .clickable {
                                            selectedImageBitmap = imageBitmap
                                            isDialogVisible = true
                                        }
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(end = 8.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "N/A",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (isDialogVisible) {
        selectedImageBitmap?.let { bitmap ->
            ShowImageZoomDialog(
                image = bitmap,
                onDismiss = { isDialogVisible = false }
            )
        }
    }
}


