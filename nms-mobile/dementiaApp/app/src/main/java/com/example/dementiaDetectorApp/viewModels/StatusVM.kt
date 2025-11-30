package com.example.dementiaDetectorApp.viewModels

import androidx.lifecycle.ViewModel
import com.example.dementiaDetectorApp.api.tests.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class StatusVM @Inject constructor(
    private val respository: TestRepository
): ViewModel() {

}