@file:OptIn(ExperimentalMaterial3Api::class)

package hr.foi.air.baufind.ui.screens.UserProfileScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hr.foi.air.baufind.service.UserProfileService.UserProfileService
import hr.foi.air.baufind.ui.components.Skill
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

@Composable
fun EditProfileButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
fun decodeBase64ToByteArray(base64: String?): ByteArray? {
    return base64?.let {
        try {
            android.util.Base64.decode(it, android.util.Base64.DEFAULT)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
fun convertUriToByteArray(context: Context, uri: Uri?): ByteArray? {
    return uri?.let {
        try {
            context.contentResolver.openInputStream(it)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
fun uriToBitmap(context: Context, uri: Uri?): Bitmap? {
    return uri?.let {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, it))
            } else {
                @Suppress("DEPRECATION")
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}



@Composable
fun userProfileScreen(
    navController: NavController,
    context: Context,
    tokenProvider: TokenProvider,
    userProfileViewModel: UserProfileViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val jwt = tokenProvider.getToken()
    val userProfileService = UserProfileService(tokenProvider)
    val userProfile by userProfileViewModel.userProfile.collectAsState()



    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                //znaci da ce jwt biti argument
                val fetchedProfile = jwt?.let { userProfileService.fetchUserProfile() }
                if (fetchedProfile != null) {
                    userProfileViewModel.setUserProfile(fetchedProfile)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    if (userProfile != null) {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            "Profile",
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
            val profile = userProfile!!
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
            ) {
                UserProfileHeader(profile.name, profile.address ?: "N/A", decodeBase64ToByteArray(profile.profilePicture))
                EditProfileButton(onClick = {navController.navigate("editUserProfileScreen")})
                UserProfileContactInformation(profile.address ?: "N/A", profile.phone ?: "N/A", profile.email)
                UserSkillSection(profile.skills.orEmpty().map { skill -> Skill(skill.id, skill.title) })
                Spacer(modifier = Modifier.width(22.dp))
                profile.reviews?.let {
                    UserProfileReview(
                        averageRating = it.averageRating,
                        totalReviews = it.totalReviews,
                        ratings = it.ratings,
                        onReviewsClick = { /* Navigate to Reviews Screen */ }
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
