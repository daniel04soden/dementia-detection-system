package com.example.dementiaDetectorApp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.viewModels.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel = viewModel(), modifier: Modifier = Modifier) {
    val email by viewModel.email.collectAsState()
    val pswd by viewModel.pswd.collectAsState()
    Box(
        modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .padding(top = Dp(56f))
    ){
        Box(
            modifier = Modifier
                .fillMaxSize(0.5f)
                .align(Alignment.TopCenter)
        ) {
            Column(
                modifier .background(color = Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Text(text = "Img placeholder")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight(0.5F)
                .fillMaxWidth(0.8F)
                .background(color = Color.LightGray)
                .align(Alignment.Center)
        ){
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .matchParentSize()
                    .background(color = Color.LightGray)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ){
                    TextField(
                        value = email,
                        onValueChange = {viewModel.onEmailChange(it)},
                        label = { Text(text = "Email") }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    TextField(
                        value = pswd,
                        onValueChange = {viewModel.onPswdChange(it)},
                        label = { Text(text = "Password") }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { navController.navigate("registration") }) {
                        Text(text = "Login")
                    }
                }
                Spacer(modifier = Modifier.height(Dp(30f)))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Not signed up?")
                }
                Spacer(modifier = Modifier.height(Dp(10f)))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { navController.navigate("registration") }) {
                        Text(text = "Sign up")
                    }
                }
            }
        }
    }
}