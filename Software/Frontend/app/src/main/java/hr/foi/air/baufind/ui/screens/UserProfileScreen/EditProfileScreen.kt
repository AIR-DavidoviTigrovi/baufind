@file:OptIn(ExperimentalMaterial3Api::class)

package hr.foi.air.baufind.ui.screens.UserProfileScreen

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
        var skills by remember { mutableStateOf(profile.skills.orEmpty().toMutableList()) }
        var profilePicture by remember { mutableStateOf(profile.profilePicture) }

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
                // Profile Picture Upload
                Text(
                    text = "Tap to Upload Profile Picture",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                        .height(150.dp)
                ) {
                    // Placeholder for Profile Picture
                    // TODO: Replace with actual image picker
                    Button(
                        onClick = {
                            // Add logic to open the gallery and select a picture
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Select Profile Picture")
                    }
                }


                Spacer(Modifier.height(16.dp))

                // Editable Text Fields
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
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
                Spacer(Modifier.height(8.dp))

                // Skill Management
                Text("Skills", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(8.dp))
                Column {
                    skills.forEachIndexed { index, skill ->
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
                                skills.removeAt(index)
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
                        onClick = { skills.add(SkillResponse(id = skills.size + 1, title = "New Skill")) },
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
                        val updatedProfile = profile.copy(
                            name = name,
                            address = address,
                            phone = phone,
                            email = email,
                            profilePicture = profilePicture,
                            skills = skills
                        )
                        userProfileViewModel.setUserProfile(updatedProfile)
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
