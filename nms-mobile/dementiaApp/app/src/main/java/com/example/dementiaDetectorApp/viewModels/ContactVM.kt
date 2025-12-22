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
import org.json.JSONObject
import java.net.URLEncoder
import java.net.URL

@HiltViewModel
class ContactVM @Inject constructor(
    private val repository: ClinicRepository
) : ViewModel() {

    var isLoading = false
        private set

    private val dummyClinic = Clinic(
        clinicID = 1,
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
                val encoded = URLEncoder.encode("$eircode, Ireland", "UTF-8")
                val url = "https://geocode.search.hereapi.com/v1/geocode?q=$encoded&apiKey=p2of8s3Sclzoqcwxvy2a9i7X4A_H_HgaZzZLD-Xo1lQ&country=IRL"

                val connection = URL(url).openConnection() as java.net.HttpURLConnection
                connection.setRequestProperty("User-Agent", "DementiaDetectorApp/1.0")
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val response = connection.inputStream.bufferedReader().readText()
                val jsonObject = JSONObject(response)
                val items = jsonObject.getJSONArray("items")

                if (items.length() > 0) {
                    val first = items.getJSONObject(0)
                    val position = first.getJSONObject("position")
                    val lat = position.getDouble("lat")
                    val lon = position.getDouble("lng")
                    _coords.value = lat to lon
                } else {
                    _coords.value = null
                }
            } catch (e: Exception) {
                _coords.value = null
            }
        }
    }

}
