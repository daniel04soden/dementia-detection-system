package com.example.dementiaDetectorApp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dementiaDetectorApp.ui.theme.AppTheme
import com.example.dementiaDetectorApp.ui.views.ContactScreen
import com.example.dementiaDetectorApp.ui.views.HomeScreen
import com.example.dementiaDetectorApp.ui.views.LoginScreen
import com.example.dementiaDetectorApp.ui.views.QuestionnaireScreen
import com.example.dementiaDetectorApp.ui.views.RegistrationScreen
import com.example.dementiaDetectorApp.ui.views.RiskScreen
import com.example.dementiaDetectorApp.ui.views.SpeechScreen
import com.example.dementiaDetectorApp.ui.views.Stage1Screen
import com.example.dementiaDetectorApp.ui.views.Stage2Screen
import com.example.dementiaDetectorApp.ui.views.StatusScreen
import com.example.dementiaDetectorApp.viewModels.AuthViewModel
import com.example.dementiaDetectorApp.viewModels.ContactVM
import com.example.dementiaDetectorApp.viewModels.HomeVM
import com.example.dementiaDetectorApp.viewModels.QViewModel
import com.example.dementiaDetectorApp.viewModels.SharedVM
import com.example.dementiaDetectorApp.viewModels.SpeechViewModel
import com.example.dementiaDetectorApp.viewModels.Stage1VM
import com.example.dementiaDetectorApp.viewModels.Stage2VM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchPoint : ComponentActivity() {

    private val recordPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(this, "Microphone permission required for recording", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                val nc = rememberNavController()
                val sharedVM: SharedVM = hiltViewModel()
                val homeVM: HomeVM = hiltViewModel()
                val authVM: AuthViewModel = hiltViewModel()
                val qVM : QViewModel = hiltViewModel()
                val t1VM:  Stage1VM = hiltViewModel()
                val t2VM: Stage2VM = hiltViewModel()
                val sVM: SpeechViewModel = hiltViewModel()
                val cVM: ContactVM = hiltViewModel()

                NavHost(navController = nc, startDestination = "speech") {
                    composable("login"){LoginScreen(authVM,sharedVM, nc)}
                    composable("registration"){ RegistrationScreen(authVM,sharedVM, nc) }
                    composable("home"){ HomeScreen(homeVM, sharedVM, nc) }
                    composable("questionnaire"){QuestionnaireScreen(qVM, nc)}
                    composable("test1"){ Stage1Screen(t1VM, sharedVM, nc)}
                    composable("test2"){ Stage2Screen(t2VM, sharedVM, nc)}
                    composable("speech"){SpeechScreenWithPermission(sVM, sharedVM, nc, ::requestMicPermission)}
                    composable("status"){ StatusScreen(sharedVM, nc) }
                    composable("risk"){ RiskScreen(sharedVM,nc) }
                    composable("contact") { ContactScreen(cVM, sharedVM, nc) }
                }
            }
        }
    }

    private fun requestMicPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED -> {
                // Already granted
            }
            else -> {
                recordPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }
}

// Wrapper for SpeechScreen that handles permission
@Composable
private fun SpeechScreenWithPermission(
    sVM: SpeechViewModel,
    sharedVM: SharedVM,
    nc: NavController,
    requestPermission: () -> Unit
) {
    // Request permission only once when screen first loads
    LaunchedEffect(Unit) {
        requestPermission()
    }

    SpeechScreen(sVM, sharedVM, nc)
}
