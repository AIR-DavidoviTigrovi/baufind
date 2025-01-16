package hr.foi.air.baufind.ui.screens.WorkerSearchScreen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.foi.air.baufind.helpers.PictureHelper.Companion.decodeBase64ToByteArray
import hr.foi.air.baufind.service.UserProfileService.UserProfileService
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ui.components.Skill
import hr.foi.air.baufind.ui.screens.UserProfileScreen.UserProfileReview
import hr.foi.air.baufind.ui.screens.UserProfileScreen.UserSkillSection
import hr.foi.air.baufind.ui.screens.UserProfileScreen.byteArrayToBitmap
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.response.UserProfileResponse
import kotlinx.coroutines.launch

@Composable
fun WorkerProfileScreen(
    navController: NavController,
    context: Context,
    tokenProvider: TokenProvider,
    id: Int,
    jobId : Int,
    skillId : Int,
    viewModel: WorkerSearchViewModel
)  {
    val coroutineScope = rememberCoroutineScope()
    val userProfileService = UserProfileService(tokenProvider)
    viewModel.tokenProvider.value = tokenProvider
    val scrollState = rememberScrollState()
    val userProfile = remember { mutableStateOf<UserProfileResponse?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(id) {
        coroutineScope.launch {
            val response = userProfileService.getUserProfileById(id)
            userProfile.value = response
            isLoading.value = false
        }
    }

    when {
        isLoading.value -> CircularProgressIndicator()
        userProfile.value != null ->
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    if (userProfile.value!!.profilePicture != null) {
                        val bitmap =
                            byteArrayToBitmap(decodeBase64ToByteArray(userProfile.value!!.profilePicture))
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().clip(CircleShape)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Placeholder",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(40.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    userProfile.let {
                        it.value?.let { it1 ->
                            Text(
                                text = it1.name,
                                color = MaterialTheme.colorScheme.primary,
                                style = TextStyle(
                                    fontSize = 20.sp
                                ),
                                textAlign = TextAlign.Center,
                                fontWeight = Bold,
                                fontSize = 22.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    userProfile.value!!.address?.let {
                        Text(
                            text = it, color = MaterialTheme.colorScheme.primary,
                            style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                            textAlign = TextAlign.Center, fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Text(
                        text = "Joined " + userProfile.value!!.joined, color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                        textAlign = TextAlign.Center, fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Contact Information",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = "Email Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = userProfile.value!!.email,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Phone,
                                contentDescription = "Phone Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        userProfile.value!!.phone?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    UserSkillSection(
                        skills = userProfile.value!!.skills.orEmpty().map { skill ->
                            Skill(skill.id, skill.title)
                        }
                    )


                    Spacer(modifier = Modifier.height(16.dp))

                    userProfile.value!!.reviews?.let { reviews ->
                        UserProfileReview(
                            averageRating = reviews.averageRating,
                            totalReviews = reviews.totalReviews,
                            ratings = reviews.ratings,
                            onReviewsClick = {
                                navController.navigate("reviewsScreen/${userProfile.value!!.id}")
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        text = "Pozovi na posao",
                        maxWidth = true,
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val response = viewModel.callWorkerToJob(
                                        jobId,
                                        skillId,
                                        userProfile.value!!.id
                                    )

                                    if (response.success) {
                                        viewModel.skillsId.value.remove(skillId)
                                        viewModel.skillsId.value = viewModel.skillsId.value
                                        if (viewModel.skillsId.value.isEmpty()) viewModel.isEmptyList = true

                                        navController.popBackStack()
                                    }
                                    else {
                                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Error: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    )
                }
            }
        else -> Text(text = "Error: Could not load profile", color = Color.Red)
    }
}