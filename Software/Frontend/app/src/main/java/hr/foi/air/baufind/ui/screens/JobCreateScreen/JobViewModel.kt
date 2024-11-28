package hr.foi.air.baufind.ui.screens.JobCreateScreen

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class JobPosition(
    val name: String,
    var count: MutableState<Int> = mutableIntStateOf(0)
)

class JobViewModel : ViewModel(){
    val jobName = mutableStateOf("")
    val jobDescription = mutableStateOf("")
    val allowInvitations = mutableStateOf(false)
    val selectedImages = mutableStateListOf<Uri>()
    val jobPositions = mutableStateListOf<JobPosition>()
    val lat = mutableDoubleStateOf(0.0)
    val long = mutableDoubleStateOf(0.0)
}
