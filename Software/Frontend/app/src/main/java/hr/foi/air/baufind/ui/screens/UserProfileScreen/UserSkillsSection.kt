@file:OptIn(ExperimentalLayoutApi::class)

package hr.foi.air.baufind.ui.screens.UserProfileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.foi.air.baufind.ui.components.Skill

@Composable
fun UserSkillSection(skills : List<Skill>){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp))
    {
        Text(
            text = "Skills",
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.ExtraBold),
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.primary
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)

        ){
          skills.forEach{ skill ->
              Box(modifier = Modifier
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer)
                  .padding(horizontal = 12.dp, vertical = 6.dp)
              ){
                  Text(text = skill.name, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.primary)
              }

          }
        }
    }
}