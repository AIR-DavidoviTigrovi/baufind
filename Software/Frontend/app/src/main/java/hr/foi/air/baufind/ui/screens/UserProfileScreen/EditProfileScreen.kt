@file:OptIn(ExperimentalMaterial3Api::class)

package hr.foi.air.baufind.ui.screens.UserProfileScreen

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.response.SkillResponse


@Composable
fun EditProfileScreen(
    navController: NavController,
    context: Context,
    tokenProvider: TokenProvider,
    userProfileViewModel: UserProfileViewModel
) {
    val userProfile by userProfileViewModel.userProfile.collectAsState()

    userProfile?.let { profile ->
        var name by remember { mutableStateOf(profile.name) }
        var address by remember { mutableStateOf(profile.address ?: "") }
        var phone by remember { mutableStateOf(profile.phone ?: "") }
        var email by remember { mutableStateOf(profile.email) }
        var profilePictureUri by remember { mutableStateOf<Uri?>(null) }
        var selectedSkills by remember { mutableStateOf(profile.skills.orEmpty().toMutableList()) }
        val initialSkills = profile.skills.orEmpty()

        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                profilePictureUri = uri
            }
        }
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Edit Profile", style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .fillMaxWidth(0.5f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        val bitmap = uriToBitmap(context, profilePictureUri)
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                text = "Select Picture",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Upload Picture")
                }

                // Editable Text Fields
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))

                // Skills Management
                Text("Skills", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(8.dp))
                Column {
                    selectedSkills.forEachIndexed { index, skill ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = skill.title,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            IconButton(onClick = {
                                selectedSkills.remove(skill)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove Skill",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                    Button(
                        onClick = { selectedSkills.add(SkillResponse(id = selectedSkills.size + 1, title = "New Skill")) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Add Skill")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Save Button
                Button(
                    onClick = {
                        val addSkills = selectedSkills.filter { it !in initialSkills }.map { it.id }
                        val removeSkills = initialSkills.filter { it !in selectedSkills }.map { it.id }
                        val byteArray = convertUriToByteArray(context, profilePictureUri)
                        Log.d("ProfilePicture", "Byte array size: ${byteArray?.size ?: 0}")

                        userProfileViewModel.updateUserProfile(
                            userId = profile.id,
                            name = name,
                            address = address,
                            phone = phone,
                            profilePicture = convertUriToByteArray(context, profilePictureUri)?.let {
                                Base64.encodeToString(it, Base64.NO_WRAP)
                            },
                            addSkills = addSkills,
                            removeSkills = removeSkills,
                            tokenProvider = tokenProvider
                        )
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Save")
                }
            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}
