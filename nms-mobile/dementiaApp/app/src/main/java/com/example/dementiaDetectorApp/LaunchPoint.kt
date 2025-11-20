package com.example.dementiaDetectorApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dementiaDetectorApp.ui.theme.AppTheme
import com.example.dementiaDetectorApp.ui.views.HomeScreen
import com.example.dementiaDetectorApp.ui.views.LoginScreen
import com.example.dementiaDetectorApp.ui.views.QuestionnaireScreen
import com.example.dementiaDetectorApp.ui.views.RegistrationScreen
import com.example.dementiaDetectorApp.ui.views.TestScreen
import com.example.dementiaDetectorApp.viewModels.AuthViewModel
import com.example.dementiaDetectorApp.viewModels.HomeVM
import com.example.dementiaDetectorApp.viewModels.QViewModel
import com.example.dementiaDetectorApp.viewModels.SharedVM
import com.example.dementiaDetectorApp.viewModels.TestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchPoint : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {

                //Nav Controller
                val nc = rememberNavController()

                //View Models
                val sharedVM: SharedVM = viewModel()
                val homeVM: HomeVM = viewModel()
                val authVM: AuthViewModel = viewModel()
                val qVM : QViewModel = viewModel()
                val tVM:  TestViewModel = viewModel()

                NavHost(navController = nc, startDestination = "login") {
                    composable("login"){LoginScreen(authVM,sharedVM, nc)}
                    composable("registration"){RegistrationScreen(nc, authVM)}
                    composable("home"){ HomeScreen(homeVM, sharedVM, nc) }
                    composable("questionnaire"){QuestionnaireScreen(nc, qVM)}
                    composable("test"){ TestScreen(nc, tVM)}
                }
            }
        }
    }
}