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
import com.example.dementiaDetectorApp.ui.views.Stage1Screen
import com.example.dementiaDetectorApp.ui.views.Stage2Screen
import com.example.dementiaDetectorApp.viewModels.AuthViewModel
import com.example.dementiaDetectorApp.viewModels.HomeVM
import com.example.dementiaDetectorApp.viewModels.QViewModel
import com.example.dementiaDetectorApp.viewModels.Stage1VM
import com.example.dementiaDetectorApp.viewModels.SharedVM
import com.example.dementiaDetectorApp.viewModels.Stage2VM
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
                val t1VM:  Stage1VM = viewModel()
                val t2VM: Stage2VM = viewModel()

                NavHost(navController = nc, startDestination = "home") {
                    composable("login"){LoginScreen(authVM,sharedVM, nc)}
                    composable("registration"){ RegistrationScreen(authVM,nc) }
                    composable("home"){ HomeScreen(homeVM, sharedVM, nc) }
                    composable("questionnaire"){QuestionnaireScreen(qVM, nc)}
                    composable("test1"){ Stage1Screen(t1VM, sharedVM, nc)}
                    composable("test2"){ Stage2Screen(t2VM, sharedVM, nc)}
                }
            }
        }
    }
}