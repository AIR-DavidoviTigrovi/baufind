package hr.foi.air.baufind.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.foi.air.baufind.R
import hr.foi.air.baufind.service.SkillsService.SkillsService
import hr.foi.air.baufind.ui.screens.JobRoom.JobRoomScreen
import hr.foi.air.baufind.ui.theme.LightPrimary
import hr.foi.air.baufind.ws.model.JobRoom
import hr.foi.air.baufind.ws.model.Skill
import hr.foi.air.baufind.ws.network.TokenProvider

@Composable
fun RoleInJobCard(allowedInvitations: Boolean,listOfSkills: List<Skill>,navController: NavController,peopleInRoom: Map<String, String>, onItemClick: () -> Unit) {
    val helperMap = mutableMapOf<String, String>()
    for (map in peopleInRoom) {
        helperMap[map.value] = map.key
    }
    Column {

        for (person in helperMap) {
            Column {
                Text(person.key, modifier = Modifier.padding(6.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                for (worker in peopleInRoom) {
                    if(worker.value == person.key){
                        if(worker.key != "Nema radnika")
                            PersonInRoomCard(workerName = worker.key, onItemClick = onItemClick)

                    }

                }
                if(person.value == "Nema radnika") {
                    if (allowedInvitations == true) {
                        Button(modifier = Modifier.padding(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LightPrimary,
                                contentColor = Color.White
                            ),
                            onClick = {
                                var helperList: MutableList<Int> = mutableListOf()
                                for (skill in listOfSkills) {
                                    if (skill.title == person.key) {
                                        helperList.add(skill.id)
                                    }
                                }
                                helperList = helperList.distinct().toMutableList()
                                navController.navigate("workersSearchScreen/${helperList}")
                            }) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_person_icon),
                                contentDescription = "Icon",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun RoleInJobCard() {
    RoleInJobCard(false,listOf(Skill(1,"Električar"),Skill(2,"Vodoinstalater")),navController = NavController(LocalContext.current),mapOf("David Matijanić" to "Vodoinstalater","Viktor Lovrić" to "Električar","Frano Šimić" to "Vodoinstalater","Nema radnika" to "Vodoinstalater"
        ,"Nema radnika" to "Vodoinstalater"),{})
}