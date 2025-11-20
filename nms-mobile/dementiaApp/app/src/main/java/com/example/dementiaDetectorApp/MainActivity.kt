package com.example.dementiaDetectorApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dementiaDetectorApp.ui.views.LoginScreen
import com.example.dementiaDetectorApp.ui.views.RegistrationScreen
import com.example.dementiaDetectorApp.ui.theme.appTheme
import com.example.dementiaDetectorApp.ui.views.NewHomeScreen
import com.example.dementiaDetectorApp.ui.views.QuestionnaireScreen
import com.example.dementiaDetectorApp.ui.views.TestScreen
import com.example.dementiaDetectorApp.viewModels.AuthViewModel
import com.example.dementiaDetectorApp.viewModels.HomeVM
import com.example.dementiaDetectorApp.viewModels.QViewModel
import com.example.dementiaDetectorApp.viewModels.SharedVM
import com.example.dementiaDetectorApp.viewModels.TestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            appTheme {
                val navController = rememberNavController()
                val sharedVM: SharedVM = viewModel()
                val homeVM: HomeVM = viewModel()
                val authViewModel: AuthViewModel = viewModel()
                val qViewModel : QViewModel = viewModel()
                val tViewModel:  TestViewModel = viewModel()
                        NavHost(navController = navController, startDestination = "home") {
                        composable("login"){LoginScreen(navController, authViewModel)}
                        composable("registration"){RegistrationScreen(navController, authViewModel)}
                        composable("home"){ NewHomeScreen(homeVM, sharedVM, navController) }
                        composable("questionnaire"){QuestionnaireScreen(navController, qViewModel)}
                        composable("test"){ TestScreen(navController, tViewModel)}
                        }
            }
        }
    }
}