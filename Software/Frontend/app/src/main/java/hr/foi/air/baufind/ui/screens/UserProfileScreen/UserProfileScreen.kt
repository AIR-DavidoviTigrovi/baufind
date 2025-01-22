@file:OptIn(ExperimentalMaterial3Api::class)

package hr.foi.air.baufind.ui.screens.UserProfileScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hr.foi.air.baufind.helpers.PictureHelper
import hr.foi.air.baufind.service.AuthService.AuthService
import hr.foi.air.baufind.service.UserProfileService.UserProfileService
import hr.foi.air.baufind.service.jwtService.JwtService
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ui.components.Skill
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

@Composable
fun ProfileButtons(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Button(
                modifier = Modifier
                    .height(48.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                onClick = {
                    navController.navigate("editUserProfileScreen")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(
                    text = "Uredi profil",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
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
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, it))
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
    userProfileViewModel: UserProfileViewModel,
    userId: Int?
) {
    val coroutineScope = rememberCoroutineScope()
    val jwt = tokenProvider.getToken()
    val userProfileService = UserProfileService(tokenProvider)
    val userProfile by userProfileViewModel.userProfile.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val fetchedProfile = if (userId == null || userId == -1) {
                    jwt?.let { userProfileService.fetchUserProfile() }
                } else {
                    userProfileService.getUserProfileById(userId)
                }
                if (fetchedProfile != null) {
                    userProfileViewModel.setUserProfile(fetchedProfile)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    val isOwnProfile = (userId == null || userId == -1)
    if (userProfile != null) {

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            "Profil",
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
                                contentDescription = "Natrag",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }, actions = {
                        if (isOwnProfile) {
                            Box {
                                IconButton(onClick = { showMenu = !showMenu }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Postavke",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Postavke") },
                                        onClick = {
                                            navController.navigate("settingsScreen")
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Izbriši račun") },
                                        onClick = {
                                            showMenu = false
                                            showDeleteConfirmation = true
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Odjava") },
                                        onClick = {
                                            showMenu = false
                                            val authService = AuthService(tokenProvider)
                                            coroutineScope.launch {
                                                val response = authService.logoutAsync()
                                                JwtService.clearJwt(context)
                                                navController.navigate("login") {
                                                    popUpTo(0) { inclusive = true }
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            },

        ) { paddingValues ->
            val profile = userProfile!!
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
            ) {
                UserProfileHeader(profile.name, profile.address ?: "N/A", PictureHelper.decodeBase64ToByteArray(profile.profilePicture))
                if (isOwnProfile) {
                    ProfileButtons(navController)
                }
                UserProfileContactInformation(profile.address ?: "N/A", profile.phone ?: "N/A", profile.email)
                UserSkillSection(profile.skills.orEmpty().map { skill -> Skill(skill.id, skill.title) })
                Spacer(modifier = Modifier.width(22.dp))
                profile.reviews?.let {
                    UserProfileReview(
                        averageRating = it.averageRating,
                        totalReviews = it.totalReviews,
                        ratings = it.ratings,
                        onReviewsClick = { navController.navigate("reviewsScreen/${profile.id}") }
                    )
                }
            }
        }
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Izbriši račun") },
                text = { Text("Jeste li sigurni da želite izbrisati račun? Ovo se ne može poništiti!") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                val deleteResponse = userProfileService.deleteUser()
                                if (deleteResponse?.success == true) {
                                    JwtService.clearJwt(context)
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                } else {
                                    snackbarHostState.showSnackbar("Neuspjeh kod brisanja računa. Pokušajte ponovno!")
                                }
                            }
                            showDeleteConfirmation = false
                        }
                    ) {
                        Text("Briši")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmation = false }) {
                        Text("Odustani")
                    }
                }
            )
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
