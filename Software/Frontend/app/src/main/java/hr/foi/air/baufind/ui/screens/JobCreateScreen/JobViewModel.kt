package hr.foi.air.baufind.ui.screens.JobCreateScreen

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class JobPosition(
    val name: String,
    var count: Int
)

class JobViewModel : ViewModel(){
    val jobName = mutableStateOf("")
    val jobDescription = mutableStateOf("")
    val allowInvitations = mutableStateOf(false)
    val selectedImages = mutableStateListOf<Uri>()
    val jobPositions = mutableStateListOf<JobPosition>()
}
