package hr.foi.air.baufind.ui.screens.JobCreateScreen

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hr.foi.air.baufind.ws.network.TokenProvider
import java.io.InputStream

data class JobPosition(
    val name: String,
    var count: MutableState<Int> = mutableIntStateOf(0),
    val id : Int
)

class JobViewModel : ViewModel(){
    val jobName = mutableStateOf("")
    val jobDescription = mutableStateOf("")
    val allowInvitations = mutableStateOf(false)
    val selectedImages = mutableStateListOf<Uri>()
    val jobPositions = mutableStateListOf<JobPosition>()
    val lat = mutableDoubleStateOf(0.0)
    val long = mutableDoubleStateOf(0.0)
    val location = mutableStateOf("")
    val tokenProvider: MutableState<TokenProvider?> = mutableStateOf(null)

    fun getImagesAsByteArrayList(context: Context): List<ByteArray> {
        val contentResolver: ContentResolver = context.contentResolver
        val byteArrayList = mutableListOf<ByteArray>()

        for (uri in selectedImages) {
            try {
                val inputStream: InputStream? = contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    val byteArray = inputStream.readBytes()
                    byteArrayList.add(byteArray)
                    inputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return byteArrayList
    }

    fun getPositionsArray() : List<Int> {
        return jobPositions.flatMap { jobPosition ->
            List(jobPosition.count.value) { jobPosition.id }
        }
    }
}
