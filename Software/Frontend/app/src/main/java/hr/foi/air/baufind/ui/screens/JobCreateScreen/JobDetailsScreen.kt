package hr.foi.air.baufind.ui.screens.JobCreateScreen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hr.foi.air.baufind.R
import hr.foi.air.baufind.ui.components.PictureItem
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ui.components.PrimaryTextField

@Composable
fun JobDetailsScreen(navController: NavController, jobViewModel: JobViewModel){
    var jobNameError by remember { mutableStateOf("") }
    var jobDescriptionError by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            val selectedUri = data?.data
            if(selectedUri != null){
                jobViewModel.selectedImages.add(selectedUri)
            }
        }
    }

    fun validateInputs(): Boolean {
        var valid = true

        jobNameError =""
        jobDescriptionError=""

        if (jobViewModel.jobName.value.isBlank()) {
            jobNameError = "Morate unijeti ime posla"
            valid = false
        }
        if (jobViewModel.jobDescription.value.isBlank()) {
            jobDescriptionError = "Morate unijeti opis posla"
            valid = false
        }
        return valid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text ="Novi posao",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryTextField(
            value = jobViewModel.jobName.value,
            onValueChange = { jobViewModel.jobName.value = it },
            label = "Naslov posla",
            modifier = Modifier.fillMaxWidth(),
            isError = jobNameError.isNotEmpty(),
            errorMessage = jobNameError
        )
        PrimaryTextField(
            value = jobViewModel.jobDescription.value,
            onValueChange = { jobViewModel.jobDescription.value = it },
            label = "Opis posla",
            modifier = Modifier.fillMaxWidth(),
            isError = jobDescriptionError.isNotEmpty(),
            errorMessage = jobDescriptionError
        )
        PrimaryButton(
            text = "Učitaj fotografije",
            maxWidth = true,
            drawableId = R.drawable.upload_file_icon,
            onClick = {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                launcher.launch(intent)
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Fotografije",
            modifier = Modifier.align(Alignment.Start),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))
        LazyRow {
            items(jobViewModel.selectedImages){ imageUri ->
                PictureItem(imageUri = imageUri)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Dopusti pozivanja od strane radnika",
            modifier = Modifier.align(Alignment.Start),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))
        Switch(
            checked = jobViewModel.allowInvitations.value,
            onCheckedChange = { jobViewModel.allowInvitations.value = it },
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = "Nastavi",
            onClick = {
                if (validateInputs()) {
                    navController.navigate("jobPositionsLocationScreen")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun JobDetailsScreenPreview() {
    val navController = rememberNavController()
    JobDetailsScreen(navController, JobViewModel())
}