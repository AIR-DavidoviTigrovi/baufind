@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package hr.foi.air.baufind.ui.screens.UserProfileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.foi.air.baufind.ui.components.Skill
import hr.foi.air.baufind.ui.theme.BaufindTheme


@Composable
fun userProfileScreen(name: String,phone: String, address:String, email: String, profilePicture: ByteArray?){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(modifier = Modifier.fillMaxWidth(),
                title = { Text("Profile", fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    ))  },
                navigationIcon = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                // settings gumb dodaj
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ){ paddingValues ->
        val skills = listOf(
        Skill("Skill 1"),
        Skill("Skill 2"),
        Skill("Skill 3"),
        Skill("Skill 4"),
        Skill("Skill 5"),
        Skill("Skill 6"),
        Skill("Skill 7"),
        Skill("Skill 8")
    )

        Column(modifier = Modifier.padding(paddingValues).fillMaxWidth().background(MaterialTheme.colorScheme.background))
        {
            UserProfileHeader(name, address, profilePicture)
            EditProfileButton(onClick = { /* Navigate to Edit Profile Screen */ })
            UserProfileContactInformation(address, phone, email)
            UserSkillSection(skills)
            Spacer(modifier = Modifier.width(22.dp))
            UserProfileReview(averageRating = 4.5, totalReviews = 100, ratings = listOf(60, 25, 10, 3, 2), onReviewsClick = { /* Sredi navigaciju */ })
        }
    }
}

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
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
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


@Preview(showBackground = true, apiLevel = 34)
@Composable
fun userProfileScreenPreview(){
    BaufindTheme(darkTheme = false) {
        userProfileScreen(
            name = "Lily",
            phone = "123-456-7890",
            address = "San Francisco, CA",
            email = "lily@example.com",
            profilePicture = null
        )
    }
}