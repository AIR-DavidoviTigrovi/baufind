@file:OptIn(ExperimentalMaterial3Api::class)

package hr.foi.air.baufind.ui.screens.UserProfileScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import hr.foi.air.baufind.service.SkillsService.SkillsService
import hr.foi.air.baufind.ws.model.Skill
import hr.foi.air.baufind.ws.network.TokenProvider


fun decodeBase64ToBitmap(base64: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun EditProfileScreen(
    navController: NavController,
    context: Context,
    tokenProvider: TokenProvider,
    userProfileViewModel: UserProfileViewModel,
) {
    val userProfile by userProfileViewModel.userProfile.collectAsState()
    val skillsService = SkillsService(tokenProvider)

    val allSkills = remember { mutableStateOf<List<Skill>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    val selectedSkills = remember { mutableStateListOf<Skill>() }
    val addSkills = remember { mutableStateListOf<Skill>() }
    val removeSkills = remember { mutableStateListOf<Skill>() }

    LaunchedEffect(Unit) {
        allSkills.value = skillsService.fetchAllSkills().orEmpty()
        userProfile?.skills?.let { selectedSkills.addAll(it) }
    }

    userProfile?.let { profile ->
        var name by remember { mutableStateOf(profile.name) }
        var address by remember { mutableStateOf(profile.address ?: "") }
        var phone by remember { mutableStateOf(profile.phone ?: "") }
        var profilePictureUri by remember { mutableStateOf<Uri?>(null) }

        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            profilePictureUri = uri
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Uredi profil", style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Natrag",
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
                    ){
                        val bitmap = if (profilePictureUri != null) {
                            uriToBitmap(context, profilePictureUri)
                        } else {
                            profile.profilePicture?.let { decodeBase64ToBitmap(it) }
                        }

                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Slika profila",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                text = "Odaberi sliku",
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
                    Text("Prenesi sliku")
                }


                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Ime i prezime") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Adresa") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Telefon") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))


                Text("Upravljanje ulogama", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Pretražite uloge") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))


                Text("Dostupne uloge", style = MaterialTheme.typography.bodyLarge)
                Column(modifier = Modifier.fillMaxWidth()) {
                    val filteredSkills = allSkills.value.filter {
                        it.title.contains(searchQuery, ignoreCase = true) && it !in selectedSkills
                    }
                    filteredSkills.forEach { skill ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    selectedSkills.add(skill)
                                    addSkills.add(skill)
                                    removeSkills.remove(skill)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = skill.title,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text("Dodaj", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    if (filteredSkills.isEmpty()) {
                        Text(
                            "Nije pronađeno",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))


                Text("Odabrane uloge", style = MaterialTheme.typography.bodyLarge)
                Column {
                    selectedSkills.forEach { skill ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = skill.title,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            IconButton(onClick = {
                                selectedSkills.remove(skill)
                                removeSkills.add(skill)
                                addSkills.remove(skill)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Ukloni ulogu")
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))


                Button(
                    onClick = {
                        val addedSkillIds = addSkills.map { it.id }
                        val removedSkillIds = removeSkills.map { it.id }
                        val profilePictureBase64 = convertUriToByteArray(context, profilePictureUri)?.let {
                            Base64.encodeToString(it, Base64.NO_WRAP)
                        }

                        userProfileViewModel.updateUserProfile(
                            userId = profile.id,
                            name = name,
                            address = address,
                            phone = phone,
                            profilePicture = profilePictureBase64,
                            addSkills = addedSkillIds,
                            removeSkills = removedSkillIds,
                            tokenProvider = tokenProvider
                        )
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Spremi")
                }
            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
