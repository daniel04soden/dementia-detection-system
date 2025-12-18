package com.example.dementiaDetectorApp.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.ui.composables.ClinicDropdown
import com.example.dementiaDetectorApp.ui.composables.CountyDropdown
import com.example.dementiaDetectorApp.ui.theme.DarkPurple
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.ui.theme.buttonColours
import com.example.dementiaDetectorApp.ui.theme.outLinedTFColours
import com.example.dementiaDetectorApp.viewModels.AuthViewModel
import com.example.dementiaDetectorApp.viewModels.SharedVM

@Composable
fun RegistrationScreen(aVM: AuthViewModel, sharedVM: SharedVM, nc: NavController){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MidPurple)
    ){
        Column(
            Modifier.padding(bottom = 50.dp)
        ){
            Spacer(Modifier.height(35.dp))
            FormSection(aVM, sharedVM, nc)
        }
    }
}

@Composable
private fun FormSection(aVM: AuthViewModel, sharedVM: SharedVM, nc: NavController){
    AnimatedVisibility(
        visible = !aVM.registered.collectAsState().value
    ){
        Column{
            AnimatedVisibility(
                visible = aVM.s1Visi.collectAsState().value
            ){
                Section1(aVM)
            }
            AnimatedVisibility(
                visible = aVM.s2Visi.collectAsState().value
            ){
                Section2(aVM)
            }
            AnimatedVisibility(
                visible = aVM.s3Visi.collectAsState().value
            ){
                Section3(aVM, sharedVM, nc)
            }
        }
    }
}

@Composable
private fun Section1(aVM: AuthViewModel){
    Column(Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        Spacer(Modifier.height(35.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Personal Information",
                fontSize = 30.sp,
                color = DarkPurple
            )
        }
        Spacer(Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "To begin we need some information about you",
                fontSize = 22.5.sp,
                color = MidPurple,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            )
        }

        Spacer(Modifier.height(10.dp))
        val fName = aVM.fName.collectAsState().value
        Text(
            text = "First Name",
            color = MidPurple,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = fName,
                onValueChange = {aVM.onFNameChange(it)},
                placeholder = { Text(text = "First name") },
                colors = outLinedTFColours(),
                modifier = Modifier.width(300.dp)
            )
        }

        Spacer(Modifier.height(10.dp))
        val lName = aVM.lName.collectAsState().value
        Text(
            text = "Last Name",
            color = MidPurple,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = lName,
                onValueChange = {aVM.onLNameChange(it)},
                placeholder = { Text(text = "Last name") },
                colors = outLinedTFColours(),
                modifier = Modifier.width(300.dp)
            )
        }

        Spacer(Modifier.height(10.dp))
        val num = aVM.phoneNum.collectAsState().value
        Text(
            text = "Phone Number",
            color = MidPurple,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = num,
                onValueChange = {
                    if (it.length>=10) aVM.onPhoneNumChange(it.take(10))
                    else aVM.onPhoneNumChange(it)
                },
                placeholder = { Text(text = "Phone Number") },
                colors = outLinedTFColours(),
                modifier = Modifier.width(300.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(Modifier.height(10.dp))
        val eir = aVM.eircode.collectAsState().value
        Text(
            text = "Eircode",
            color = MidPurple,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = eir,
                onValueChange = {aVM.onEircodeChange(it)},
                placeholder = { Text(text = "Eircode") },
                colors = outLinedTFColours(),
                modifier = Modifier.width(300.dp)
            )
        }

        Row(modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    if(aVM.validateS1()){
                        aVM.onS1VisiChange(false)
                        aVM.onS2VisiChange(true)
                    }
                },
                colors = buttonColours(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .width(300.dp)
                    .padding(bottom = 25.dp)
            ) {
                Text(text = "Next Step", fontSize = 25.sp, color = Color.White)
            }
        }
    }
}

@Composable
private fun Section2(aVM: AuthViewModel){
    Column(Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        Spacer(Modifier.height(35.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Login Information",
                fontSize = 30.sp,
                color = DarkPurple
            )
        }
        Spacer(Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Next we need to set up your login credentials",
                fontSize = 22.5.sp,
                color = MidPurple,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            )
        }

        Spacer(Modifier.height(10.dp))
        val email = aVM.email.collectAsState().value
        Text(
            text = "Email",
            color = MidPurple,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = email,
                onValueChange = {aVM.onEmailChange(it)},
                placeholder = { Text(text = "Email") },
                colors = outLinedTFColours(),
                modifier = Modifier.width(300.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        }

        Spacer(Modifier.height(10.dp))
        val pswd = aVM.pswd.collectAsState().value
        Text(
            text = "Password",
            color = MidPurple,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = pswd,
                onValueChange = {aVM.onPswdChange(it)},
                placeholder = { Text(text = "Password") },
                colors = outLinedTFColours(),
                modifier = Modifier.width(300.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )
        }

        Spacer(Modifier.height(10.dp))
        val conf = aVM.confPswd.collectAsState().value
        Text(
            text = "Confirm Password",
            color = MidPurple,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = conf,
                onValueChange = {aVM.onConfPswdChange(it)},
                placeholder = { Text(text = "Confirm Password") },
                colors = outLinedTFColours(),
                modifier = Modifier.width(300.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )
        }

        Row(modifier = Modifier
            .fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    if(aVM.validateS2()){
                        aVM.onS2VisiChange(false)
                        aVM.onS3VisiChange(true)
                    }
                },
                colors = buttonColours(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .width(300.dp)
                    .padding(bottom = 25.dp)
            ) {
                Text(text = "Next Step", fontSize = 25.sp, color = Color.White)
            }
        }
    }
}

@Composable
private fun Section3(aVM: AuthViewModel, sharedVM: SharedVM, nc: NavController){
    Column(Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        Spacer(Modifier.height(35.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Clinic Information",
                fontSize = 30.sp,
                color = DarkPurple
            )
        }
        Spacer(Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lastly we need you to select whatever clinic is the most convenient for you so that we can get you in touch with medical professionals if needed",
                fontSize = 22.5.sp,
                color = MidPurple,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = "County",
            color = MidPurple,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
        val selectedCounty = aVM.county.collectAsState(initial = "Antrim").value
        CountyDropdown(
            selectedCounty = selectedCounty,
            onCountySelected = { county -> aVM.onCountyChange(county)},
            modifier = Modifier.padding(horizontal = 50.dp)
        )

        Spacer(Modifier.height(35.dp))
        Text(
            text = "Clinic",
            color = MidPurple,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
        val selectedClinic = aVM.clinic.collectAsState().value
        ClinicDropdown(
            clinics = aVM.clinics.collectAsState().value,
            selectedClinicId = selectedClinic,
            onClinicSelected = {clinic -> aVM.onClinicChange(clinic)},
            modifier = Modifier.padding(horizontal = 50.dp)
            )

        Spacer(Modifier.height(10.dp))

        Row(modifier = Modifier
            .fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    val id = aVM.signUp{nc.navigate("login")}
                    sharedVM.onIdChange(id)
                    sharedVM.updateTestList()
                },
                colors = buttonColours(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .width(300.dp)
                    .padding(bottom = 25.dp)
            ) {
                Text(text = "Sign Up", fontSize = 25.sp, color = Color.White)
            }
        }
    }
}