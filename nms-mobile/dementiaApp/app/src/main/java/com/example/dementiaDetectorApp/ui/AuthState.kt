package com.example.dementiaDetectorApp.ui

data class AuthState
    ( val isLoading: Boolean = false,
      val signUpUserName: String = "",
      val signUpPswd: String = "",
      val signInUserName: String ="",
      val signInPswd: Boolean = false
            )