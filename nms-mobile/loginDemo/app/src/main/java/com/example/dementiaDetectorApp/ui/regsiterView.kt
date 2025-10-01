package com.example.dementiaDetectorApp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.viewModels.AuthViewModel

@Composable
fun RegistrationScreen(navController: NavController, viewModel: AuthViewModel = viewModel(), modifier: Modifier = Modifier){
    val email by viewModel.email.collectAsState()
    val fName by viewModel.fName.collectAsState()
    val lName by viewModel.lName.collectAsState()
    val num by viewModel.phoneNum.collectAsState()
    val pswd by viewModel.pswd.collectAsState()
    val confPswd by viewModel.confPswd.collectAsState()

    Box(
        modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .padding(top = Dp(56f))
    ){
        Box(
            modifier = Modifier
                .fillMaxHeight(0.8F)
                .fillMaxWidth(0.8F)
                .background(color = Color.LightGray)
                .align(Alignment.TopCenter)
        ){
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .matchParentSize()
                    .background(color = Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
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
                        value = fName,
                        onValueChange = {viewModel.onFNameChange(it)},
                        label = { Text(text = "First Name") }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    TextField(
                        value = lName,
                        onValueChange = {viewModel.onLNameChange(it)},
                        label = { Text(text = "Last Name") }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    TextField(
                        value = num,
                        onValueChange = {viewModel.onPhoneNumChange(it)},
                        label = { Text(text = "Phone Number") }
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
                        .fillMaxWidth(0.8f)
                ) {
                    TextField(
                        value = confPswd,
                        onValueChange = {viewModel.onConfPswdChange(it)},
                        label = { Text(text = "Confirm password") }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.Center
                ){
                    Button(onClick = {
                        viewModel.register()
                        navController.navigate("login")}){
                        Text(text = "Sign up")
                    }
                }
            }
        }
    }
}