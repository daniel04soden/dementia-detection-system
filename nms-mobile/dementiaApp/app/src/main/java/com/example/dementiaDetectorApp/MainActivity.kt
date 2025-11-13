package com.example.dementiaDetectorApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dementiaDetectorApp.ui.views.HomeScreen
import com.example.dementiaDetectorApp.ui.views.LoginScreen
import com.example.dementiaDetectorApp.ui.views.RegistrationScreen
import com.example.dementiaDetectorApp.ui.theme.appTheme
import com.example.dementiaDetectorApp.ui.views.QuestionnaireScreen
import com.example.dementiaDetectorApp.viewModels.AuthViewModel
import com.example.dementiaDetectorApp.viewModels.QViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            appTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                val qViewModel : QViewModel = viewModel()
                        NavHost(navController = navController, startDestination = "login") {
                    composable("login"){LoginScreen(navController, authViewModel)}
                    composable("registration"){RegistrationScreen(navController, authViewModel)}
                    composable("home"){HomeScreen(navController, authViewModel)}
                    composable("questionnaire"){QuestionnaireScreen(qViewModel)}
                }
            }
        }
    }
}