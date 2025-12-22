package com.example.dementiaDetectorApp.ui.views

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.api.auth.AuthResult
import com.example.dementiaDetectorApp.ui.composables.WaveBGBox
import com.example.dementiaDetectorApp.ui.theme.buttonColours
import com.example.dementiaDetectorApp.ui.theme.outLinedTFColours
import com.example.dementiaDetectorApp.viewModels.AuthViewModel
import com.example.dementiaDetectorApp.viewModels.SharedVM

@Composable
fun LoginScreen(authVM: AuthViewModel, sharedVM: SharedVM, nc: NavController) {
    val context = LocalContext.current

    // Handle auth results with toasts and navigation
    LaunchedEffect(authVM) {
        authVM.authResults.collect { result ->
            when (result) {
                is AuthResult.Authorized -> {
                    result.data?.let { loginResponse ->
                       sharedVM.onIdChange(loginResponse.ID)
                        nc.navigate("home")
                    }
                }
                is AuthResult.Unauthorized -> {
                    Toast.makeText(context, "Invalid credentials", Toast.LENGTH_LONG).show()
                }
                is AuthResult.UnknownError -> {
                    Toast.makeText(context, "An unknown error occurred", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Box(modifier = Modifier
        .background(Color.White)
        .fillMaxSize()
    ) {
        Column {
            WaveBGBox(
                content = {
                    Spacer(Modifier.height(35.dp))
                    LogoSection()
                    Spacer(Modifier.fillMaxHeight(0.3F))
                    LoginInfoSection(authVM, sharedVM, nc)
                    Spacer(Modifier.height(35.dp))
                    RegisterSection(nc)
                }
            )
        }
    }
}

@Composable
fun LogoSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painterResource(R.drawable.logo),
            contentDescription = "DA Logo",
            tint = Color.Unspecified,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(0.95F)
        )
    }
}

@Composable
fun LoginInfoSection(authVM: AuthViewModel, sharedVM:SharedVM, nc: NavController) {
    val email = authVM.email.collectAsState().value
    val pswd = authVM.pswd.collectAsState().value

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 7.5.dp, end = 7.5.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(start = 85.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Email",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 20.sp
            )
        }
        OutlinedTextField(
            value = email,
            onValueChange = { newVal -> authVM.onEmailChange(newVal) },
            placeholder = { Text("YourEmail@example.com") },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = outLinedTFColours(),
            modifier = Modifier.width(300.dp)
        )

        Row(
            modifier = Modifier
                .padding(start = 85.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Password",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 20.sp
            )
        }
        OutlinedTextField(
            value = pswd,
            onValueChange = { newVal -> authVM.onPswdChange(newVal) },
            placeholder = { Text("********") },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = outLinedTFColours(),
            modifier = Modifier.width(300.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                val id =  authVM.signIn { nc.navigate("home") }
                sharedVM.onIdChange(id)
            },
            colors = buttonColours(),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.width(300.dp)
        ) {
            Text(text = "Login", fontSize = 25.sp, color = Color.White)
        }
    }
}

@Composable
fun RegisterSection(nc: NavController) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 7.5.dp, end = 7.5.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Don't have an account?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { nc.navigate("registration") },
            colors = buttonColours(),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.width(300.dp)
        ) {
            Text(text = "Sign Up", fontSize = 25.sp, color = Color.White)
        }
    }
}
