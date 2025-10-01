package com.example.logindemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController=navController, startDestination="login"){
                composable("login"){ Login(navController)}
                composable("registration"){ Registration(navController)}
            }

        }
    }
}

@Composable
fun Login(navController: NavController, modifier: Modifier=Modifier) {
    var uName by remember{ mutableStateOf("")}
    var pswd by remember{ mutableStateOf("")}
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
                        value = uName,
                        onValueChange = { newValue -> uName = newValue },
                        label = { Text(text = "Username") }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    TextField(
                        value = pswd,
                        onValueChange = { newValue -> pswd = newValue },
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

@Composable
fun Registration(navController: NavController, modifier: Modifier = Modifier){
    var uName by remember{ mutableStateOf("")}
    var fName by remember { mutableStateOf("")}
    var lName by remember { mutableStateOf("")}
    var pswd by remember{ mutableStateOf("")}
    var confirmPswd by remember { mutableStateOf("")}

    Box(
        modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .padding(top = Dp(56f))
    ){
        Box(
            modifier = Modifier
                .fillMaxHeight(0.5F)
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
                        value = uName,
                        onValueChange = { newValue -> uName = newValue },
                        label = { Text(text = "Username") }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    TextField(
                        value = fName,
                        onValueChange = { newValue -> fName = newValue },
                        label = { Text(text = "First Name") }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    TextField(
                        value = lName,
                        onValueChange = { newValue -> lName = newValue },
                        label = { Text(text = "Last Name") }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    TextField(
                        value = pswd,
                        onValueChange = { newValue -> pswd = newValue },
                        label = { Text(text = "Password") }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    TextField(
                        value = confirmPswd,
                        onValueChange = { newValue -> confirmPswd = newValue },
                        label = { Text(text = "Confirm password") }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.Center
                ){
                    Button(onClick = { navController.navigate("login") }) {
                        Text(text = "Sign up")
                    }
                }
            }
        }
    }
}