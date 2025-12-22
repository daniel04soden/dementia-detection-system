package com.example.dementiaDetectorApp.api.risk

import android.content.SharedPreferences
import jakarta.inject.Inject

class RiskRepoImp @Inject constructor(
    private val api:RiskAPI,
    private val prefs: SharedPreferences
):RiskRepo{

}