package com.example.dementiaDetectorApp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dementiaDetectorApp.api.clinics.ClinicRepository
import com.example.dementiaDetectorApp.models.Clinic
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.URLEncoder
import java.net.URL

@HiltViewModel
class ContactVM @Inject constructor(
    private val repository: ClinicRepository
) : ViewModel() {

    var isLoading = false
        private set

    val dummyClinic = Clinic(
        id = 1,
        name = "Greenwood Health Center",
        phone = "01234 567890",
        county = "Dublin",
        eircode = "D02 XY12"
    )

    private val _clinic = MutableStateFlow<Clinic>(dummyClinic)
    val clinic: StateFlow<Clinic> = _clinic

    private val _coords = MutableStateFlow<Pair<Double, Double>?>(null)
    val coords: StateFlow<Pair<Double, Double>?> = _coords

    fun initClinic(id: Int) {
        getClinic(id)
    }

    private fun getClinic(id: Int) {
        isLoading = true
        viewModelScope.launch {
            try {
                val res = repository.getClinic(id)
                _clinic.value = res.data ?: dummyClinic
                geocodeEircode(_clinic.value.eircode)
            } catch (e: Exception) {
                _clinic.value = dummyClinic
                geocodeEircode(dummyClinic.eircode)
            } finally {
                isLoading = false
            }
        }
    }

    private fun geocodeEircode(eircode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Encode eircode for URL
                val encoded = URLEncoder.encode("$eircode, Ireland", "UTF-8")
                val url = "https://nominatim.openstreetmap.org/search?format=json&q=$encoded"

                // Open connection with proper User-Agent
                val connection = URL(url).openConnection() as java.net.HttpURLConnection
                connection.setRequestProperty(
                    "User-Agent",
                    "DementiaDetectorApp/1.0 (youremail@example.com)"
                )
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                // Read response
                val response = connection.inputStream.bufferedReader().readText()
                val jsonArray = JSONArray(response)

                // Parse coordinates if available
                if (jsonArray.length() > 0) {
                    val first = jsonArray.getJSONObject(0)
                    val lat = first.getString("lat").toDouble()
                    val lon = first.getString("lon").toDouble()
                    _coords.value = lat to lon
                } else {
                    // fallback to Dublin city center
                    _coords.value = 53.349805 to -6.26031
                }

            } catch (e: Exception) {
                e.printStackTrace()
                // fallback coords on error
                _coords.value = 53.349805 to -6.26031
            }
        }
    }

}
