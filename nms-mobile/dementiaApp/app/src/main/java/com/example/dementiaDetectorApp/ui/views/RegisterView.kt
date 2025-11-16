package com.example.dementiaDetectorApp.ui.views

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.viewModels.AuthViewModel

@Composable
fun RegistrationScreen(navController: NavController, viewModel: AuthViewModel){
    val email by viewModel.email.collectAsState()
    val fName by viewModel.fName.collectAsState()
    val lName by viewModel.lName.collectAsState()
    val num by viewModel.phoneNum.collectAsState()
    val pswd by viewModel.pswd.collectAsState()
    val confPswd by viewModel.confPswd.collectAsState()
    val addressOne by viewModel.addressOne.collectAsState()
    val addressTwo by viewModel.addressTwo.collectAsState()
    val addressThree by viewModel.addressThree.collectAsState()
    val city by viewModel.city.collectAsState()
    val county by viewModel.county.collectAsState()
    val eircode by viewModel.eircode.collectAsState()

    var q1Visible by remember { mutableStateOf(true)}
    var q2Visible by remember { mutableStateOf(true) }
    var q3Visible by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            Box(modifier = Modifier
                .fillMaxHeight(0.1F)
                //.height(Dp(56F))
                .fillMaxWidth()
                .padding(WindowInsets.statusBars.asPaddingValues())
            ){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = Color.Magenta),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Text("Sign Up", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        },
        //bottomBar = {}
    ){ innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize(1F)
            .background(color = Color.White)
            .padding(innerPadding)
        ){
            Column(modifier = Modifier
                .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center)
            {
                Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                Column(modifier = Modifier
                    .fillMaxWidth(0.86F)
                    .fillMaxHeight(0.9F)
                    .background(color = Color.Red),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Box(modifier = Modifier.fillMaxSize())
                    {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = q3Visible,
                            enter = slideInHorizontally() + fadeIn(),
                            exit = slideOutHorizontally() + fadeOut()
                        ){
                            Column(modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.LightGray),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.1F)
                                    .background(color = Color.Green),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically)
                                {
                                    Text("Step 3: Address Details",
                                        modifier = Modifier
                                            .padding(start = Dp(15F)),
                                        color = Color.Magenta,
                                        fontSize = 20.sp
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                                Row(modifier = Modifier
                                    .fillMaxWidth(0.925F)
                                    .height(Dp(56F))
                                    .background(color = Color.Yellow),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxHeight(1F)
                                            .fillMaxWidth(1F),
                                        value = addressOne,
                                        onValueChange = { viewModel.onAddressOneChange(it)},
                                        label = { Text(text = "Address Line 1", fontSize = 18.sp) },
                                        textStyle = TextStyle(fontSize = 16.sp),
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                                Row(modifier = Modifier
                                    .fillMaxWidth(0.925F)
                                    .height(Dp(56F))
                                    .background(color = Color.Yellow),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxHeight(1F)
                                            .fillMaxWidth(1F),
                                        value = addressTwo,
                                        onValueChange = { viewModel.onAddressTwoChange(it) },
                                        label = { Text(text = "Address Line 2", fontSize = 18.sp) },
                                        textStyle = TextStyle(fontSize = 16.sp),
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                                Row(modifier = Modifier
                                    .fillMaxWidth(0.925F)
                                    .height(Dp(56F))
                                    .background(color = Color.Yellow),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxHeight(1F)
                                            .fillMaxWidth(1F),
                                        value = addressThree,
                                        onValueChange = { viewModel.onAddressThreeChange(it)},
                                        label = { Text(text = "Address Line 3", fontSize = 18.sp) },
                                        textStyle = TextStyle(fontSize = 16.sp),
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                                Row(modifier = Modifier
                                    .fillMaxWidth(0.925F)
                                    .height(Dp(56F))
                                ){
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxHeight(1F)
                                            .weight(0.55F),
                                        value = city,
                                        onValueChange = {viewModel.onCityChange(it)},
                                        label = { Text(text = "City", fontSize = 18.sp) },
                                        textStyle = TextStyle(fontSize = 16.sp),
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.White
                                        )
                                    )

                                    Spacer(modifier = Modifier.fillMaxWidth(0.025F))

                                    TextField(
                                        modifier = Modifier
                                            .fillMaxHeight(1F)
                                            .weight(0.45F),
                                        value = county,
                                        onValueChange = {viewModel.onCityChange(it)},
                                        label = { Text(text = "County", fontSize = 18.sp) },
                                        textStyle = TextStyle(fontSize = 16.sp),
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                                Row(modifier = Modifier
                                    .fillMaxWidth(0.925F)
                                    .height(Dp(56F))
                                ){
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(),
                                        value = eircode,
                                        onValueChange = {viewModel.onEircodeChange(it)},
                                        label = { Text(text = "Eircode", fontSize = 18.sp) },
                                        textStyle = TextStyle(fontSize = 16.sp),
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight(0.35F))
                                Row{
                                    Spacer(modifier = Modifier.fillMaxWidth(0.0125F))
                                    Button(onClick = {
                                        q2Visible = true
                                    },
                                        modifier = Modifier
                                            //.fillMaxWidth(0.45F))
                                            .weight(1F))
                                    {
                                        Text("Previous Step")
                                    }
                                    Spacer(modifier = Modifier.fillMaxWidth(0.025F))
                                    Button(onClick = {
                                        if ((addressOne!="") && (city!="") && (eircode!="")){
                                            viewModel.signUp()
                                        }else{
                                        }
                                    },
                                        modifier = Modifier
                                            .weight(1F))
                                    {
                                        Text("Submit")
                                    }
                                    Spacer(modifier = Modifier.fillMaxWidth(0.0125F))
                                }
                            }
                        }
                        androidx.compose.animation.AnimatedVisibility(                 //Slide 2
                            visible = q2Visible,
                            enter = slideInHorizontally() + fadeIn(),
                            exit = slideOutHorizontally() + fadeOut()
                        ){
                            Column(modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.LightGray),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.1F)
                                    .background(color = Color.Green),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically)
                                {
                                    Text("Step 2: Account Details",
                                        modifier = Modifier
                                            .padding(start = Dp(15F)),
                                        color = Color.Magenta,
                                        fontSize = 20.sp
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                                Row(modifier = Modifier
                                    .fillMaxWidth(0.925F)
                                    .height(Dp(56F))
                                    //.fillMaxHeight(0.07F)
                                    .background(color = Color.Yellow),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxHeight(1F)
                                            .fillMaxWidth(1F),
                                        value = email,
                                        onValueChange = { viewModel.onEmailChange(it)},
                                        label = { Text(text = "Email", fontSize = 18.sp) },
                                        textStyle = TextStyle(fontSize = 16.sp),
                                        colors = TextFieldDefaults.colors(
                                            //focusedContainerColor = Color.LightGray,
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                                Row(modifier = Modifier
                                    .fillMaxWidth(0.925F)
                                    .height(Dp(56F))
                                    .background(color = Color.Yellow),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxHeight(1F)
                                            .fillMaxWidth(1F),
                                        value = pswd,
                                        onValueChange = { viewModel.onPswdChange(it) },
                                        label = { Text(text = "Password", fontSize = 18.sp) },
                                        textStyle = TextStyle(fontSize = 16.sp),
                                        visualTransformation = PasswordVisualTransformation(),
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                                Row(modifier = Modifier
                                    .fillMaxWidth(0.925F)
                                    .height(Dp(56F))
                                    .background(color = Color.Yellow),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxHeight(1F)
                                            .fillMaxWidth(1F),
                                        value = confPswd,
                                        onValueChange = {viewModel.onConfPswdChange(it)},
                                        label = { Text(text = "Confirm Password", fontSize = 18.sp) },
                                        textStyle = TextStyle(fontSize = 16.sp),
                                        visualTransformation = PasswordVisualTransformation(),
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight(0.7F))
                                Row{
                                    Spacer(modifier = Modifier.fillMaxWidth(0.0125F))

                                    Button(onClick = {
                                        q1Visible = true
                                    },
                                        modifier = Modifier
                                            .weight(1F))
                                    {
                                        Text("Previous Step")
                                    }

                                    Spacer(modifier = Modifier.fillMaxWidth(0.025F))

                                    Button(onClick = {
                                        if ((viewModel.isValidEmail()) && (viewModel.isValidPswd()) && (pswd == confPswd)){
                                            q2Visible = false
                                        }else{

                                        }
                                    },
                                        modifier = Modifier
                                            .weight(1F))
                                    {
                                        Text("Next Step")
                                    }
                                    Spacer(modifier = Modifier.fillMaxWidth(0.0125F))
                                }
                            }
                        }
                        androidx.compose.animation.AnimatedVisibility(
                            visible = q1Visible,
                            enter = slideInHorizontally() + fadeIn(),
                            exit = slideOutHorizontally() + fadeOut()
                        ){

                            Column(modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.LightGray),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.1F)
                                    .background(color = Color.Green),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically)
                                {
                                    Text("Step 1: Personal Details",
                                        modifier = Modifier
                                            .padding(start = Dp(15F)),
                                        color = Color.Magenta,
                                        fontSize = 20.sp
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                                Row(modifier = Modifier
                                    .fillMaxWidth(0.925F)
                                    .height(Dp(56F))
                                    //.fillMaxHeight(0.07F)
                                    .background(color = Color.Yellow),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxHeight(1F)
                                            .fillMaxWidth(1F),
                                        value = fName,
                                        onValueChange = { viewModel.onFNameChange(it) },
                                        label = { Text(text = "First Name", fontSize = 18.sp) },
                                        textStyle = TextStyle(fontSize = 16.sp),
                                        colors = TextFieldDefaults.colors(
                                            //focusedContainerColor = Color.LightGray,
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                                Row(modifier = Modifier
                                    .fillMaxWidth(0.925F)
                                    .height(Dp(56F))
                                    .background(color = Color.Yellow),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxHeight(1F)
                                            .fillMaxWidth(1F),
                                        value = lName,
                                        onValueChange = { viewModel.onLNameChange(it) },
                                        label = { Text(text = "Last Name", fontSize = 18.sp) },
                                        textStyle = TextStyle(fontSize = 16.sp),
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                                Row(modifier = Modifier
                                    .fillMaxWidth(0.925F)
                                    .height(Dp(56F))
                                    .background(color = Color.Yellow),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxHeight(1F)
                                            .fillMaxWidth(1F),
                                        value = num,
                                        onValueChange = {
                                            if (it.length>=10) viewModel.onPhoneNumChange(it.take(10))
                                            else viewModel.onPhoneNumChange(it)
                                        },
                                        label = { Text(text = "Phone Number", fontSize = 18.sp) },
                                        textStyle = TextStyle(fontSize = 16.sp),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight(0.7F))
                                Button(onClick = {
                                    if ((fName != "") && (lName!="") && (viewModel.validateNum()==true)){
                                        q1Visible=false
                                        q2Visible = true
                                    }else{

                                    }
                                },
                                    modifier = Modifier
                                        .fillMaxWidth(0.5F))
                                {
                                    Text("Next Step")
                                }
                            }
                        }
                    }
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Yellow),
                verticalAlignment = Alignment.CenterVertically)
            {
                Surface(
                    shape = CircleShape,
                    color = Color.DarkGray,
                    modifier = Modifier.fillMaxWidth(0.005F)
                ){}
            }
        }
    }
}