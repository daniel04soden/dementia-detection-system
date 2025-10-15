package com.example.dementiaDetectorApp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.viewModels.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel = viewModel(), modifier: Modifier = Modifier) {
    val email by viewModel.email.collectAsState()
    val pswd by viewModel.pswd.collectAsState()

    Scaffold(
        topBar = {},
        bottomBar = {}
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .padding(innerPadding)
                .padding(Dp(20F))
                .fillMaxHeight(1F)
                .fillMaxWidth(1F)
        ) {
            Column(
                modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxWidth(1F)
                        .fillMaxHeight(0.4F)

                )
                Text(text = "Welcome", fontSize = 25.sp)

                TextField(
                    value = email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = { Text(text = "Email", fontSize = 22.sp) },
                    placeholder = { Text("YourEmail@example.com", fontSize = 18.sp) },
                    textStyle = TextStyle(fontSize = 18.sp),
                    colors = TextFieldDefaults.colors(
                        //focusedContainerColor = Color.LightGray,
                        unfocusedContainerColor = Color.White
                    )
                )
                TextField(
                    value = pswd,
                    onValueChange = { viewModel.onPswdChange(it) },
                    label = { Text(text = "Password", fontSize = 22.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = TextStyle(fontSize = 18.sp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                Column(modifier.align(Alignment.CenterHorizontally)){
                    Row(Modifier.weight(1F)) {
                        Button(
                            onClick = { navController.navigate("home") },
                            modifier = Modifier
                                .fillMaxWidth(0.9F)
                                .fillMaxHeight(0.45F)
                                .requiredHeightIn(min=Dp(45F))
                        ) {
                            Text(text = "Login", fontSize = 25.sp)
                        }
                    }
                    Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                    Text("Don't have an account?", fontSize = 25.sp)
                    Row(modifier.weight(1F)) {
                        Button(
                            onClick = { navController.navigate("registration") },
                            modifier = Modifier
                                .fillMaxWidth(0.9F)
                                .fillMaxHeight(0.45F)
                                .requiredHeightIn(min=Dp(45F))

                        ) {
                            Text(text = "Sign up", fontSize = 25.sp, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}
