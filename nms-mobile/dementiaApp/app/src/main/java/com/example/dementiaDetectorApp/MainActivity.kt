package com.example.dementiaDetectorApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dementiaDetectorApp.ui.LoginScreen
import com.example.dementiaDetectorApp.ui.NewRegistrationScreen
import com.example.dementiaDetectorApp.ui.RegistrationScreen
import com.example.dementiaDetectorApp.ui.theme.appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            appTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController) }
                    composable("registration") { NewRegistrationScreen(navController) }
                }
            }
        }
    }
}