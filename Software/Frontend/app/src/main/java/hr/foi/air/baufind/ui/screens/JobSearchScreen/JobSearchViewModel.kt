package hr.foi.air.baufind.ui.screens.JobSearchScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.baufind.service.JobSearchService.JobSearchResponse
import hr.foi.air.baufind.service.JobSearchService.JobSearchService
import hr.foi.air.baufind.ws.model.JobSearchModel
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch

class JobSearchViewModel : ViewModel(){
    var success : Boolean = false
    var message : String? = null
    val jobs = mutableStateOf(emptyList<JobSearchModel>())

    private val countyMap = mapOf(
        "Varaždin County" to "Varaždinska županija",
        "Zagreb County" to "Zagrebačka županija",
        "Krapina-Zagorje County" to "Krapinsko-zagorska županija",
        "Sisak-Moslavina County" to "Sisačko-moslavačka županija",
        "Karlovac County" to "Karlovačka županija",
        "Koprivnica-Križevci County" to "Koprivničko-križevačka županija",
        "Bjelovar-Bilogora County" to "Bjelovarsko-bilogorska županija",
        "Primorje-Gorski Kotar County" to "Primorsko-goranska županija",
        "Lika-Senj County" to "Ličko-senjska županija",
        "Virovitica-Podravina County" to "Virovitičko-podravska županija",
        "Požega-Slavonia County" to "Požeško-slavonska županija",
        "Brod-Posavina County" to "Brodsko-posavska županija",
        "Zadar County" to "Zadarska županija",
        "Osijek-Baranja County" to "Osječko-baranjska županija",
        "Šibenik-Knin County" to "Šibensko-kninska županija",
        "Vukovar-Srijem County" to "Vukovarsko-srijemska županija",
        "Split-Dalmatia County" to "Splitsko-dalmatinska županija",
        "Istria County" to "Istarska županija",
        "Dubrovnik-Neretva County" to "Dubrovačko-neretvanska županija",
        "Međimurje County" to "Međimurska županija",
        "City of Zagreb" to "Grad Zagreb"
    )

    val croatianCounties: List<String>
        get() = countyMap.values.toList()

    fun getEnglishCounty(croatianName: String): String?{
        return countyMap.entries.firstOrNull { it.value == croatianName }?.key
    }

    var tokenProvider: TokenProvider? = null
    set(value) {
        field = value
        value?.let { loadJobs(it) }
    }

    private fun loadJobs(tokenProvider: TokenProvider) {
        viewModelScope.launch {
            val service = JobSearchService()
            val response : JobSearchResponse = service.fetchJobsForCurrentUserAsync(tokenProvider)
            Log.d("JobSearchViewModel", "Response: $response")
            success = response.success
            message = response.message
            jobs.value = response.jobs
        }
    }

    fun clearData(){
        success = false
        message = null
        jobs.value = emptyList()
    }

    fun isLoading() : Boolean{
        return jobs.value.isEmpty() && message == null
    }

    fun hasError() : Boolean{
        return jobs.value.isEmpty() && message != null
    }
}