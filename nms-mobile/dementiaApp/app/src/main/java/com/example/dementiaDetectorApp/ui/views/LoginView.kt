package com.example.dementiaDetectorApp.ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.ui.theme.DarkPurple
import com.example.dementiaDetectorApp.ui.theme.LightPurple
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.ui.theme.buttonColours
import com.example.dementiaDetectorApp.ui.theme.outLinedTFColours
import com.example.dementiaDetectorApp.ui.util.WaveBGBox
import com.example.dementiaDetectorApp.ui.util.standardQuadFromTo
import com.example.dementiaDetectorApp.viewModels.AuthViewModel
import com.example.dementiaDetectorApp.viewModels.SharedVM
/* Auth Toast stuff:

 val context = LocalContext.current
    LaunchedEffect(viewModel, context){
        viewModel.authResults.collect{result ->
            when(result){
                is AuthResult.Authorized ->{
                    navController.navigate("home")
                }
                is AuthResult.Unauthorized ->{
                    Toast.makeText(context, "Not authorized", Toast.LENGTH_LONG).show()
                }
                is AuthResult.UnknownError ->{
                    Toast.makeText(context, "An unknown error occurred", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
*/

@Composable
fun LoginScreen(authVM: AuthViewModel, sharedVM: SharedVM, nc: NavController){
    Box(modifier = Modifier
        .background(Color.White)
        .fillMaxSize()
    ){
        Column{
            WaveBGBox(
                content = {
                    Spacer(Modifier.height(35.dp))
                    LogoSection()
                    Spacer(Modifier.fillMaxHeight(0.3F))
                    LoginInfoSection(authVM, nc)
                    Spacer(Modifier.height(35.dp))
                    RegisterSection(nc)
                }
            )
        }
    }
}

@Composable
fun LogoSection(){
    Column(modifier = Modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally){
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
fun LoginInfoSection(authVM: AuthViewModel, nc: NavController){
    val email = authVM.email.collectAsState().value
    val pswd = authVM.pswd.collectAsState().value
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 7.5.dp, end = 7.5.dp)
            .fillMaxWidth()
    ){
        OutlinedTextField(
            value = email,
            onValueChange = {newVal: String -> authVM.onEmailChange(newVal)},
            label = {Text("Email")},
            placeholder = {Text("YourEmail@exmaple.com")},
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = outLinedTFColours(),
            modifier = Modifier.width(300.dp)
        )

        OutlinedTextField(
            value = pswd,
            onValueChange = {newVal: String -> authVM.onPswdChange(newVal)},
            label = {Text("Password")},
            placeholder = {Text("********")},
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = outLinedTFColours(),
            modifier = Modifier.width(300.dp)
        )
        Spacer(Modifier.height(20.dp))
        Button(
            //onClick = { navController.navigate("home") },
            onClick = {authVM.signIn { nc.navigate("home") } },
            colors = buttonColours(),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .width(300.dp)
        ) {
            Text(text = "Login", fontSize = 25.sp, color = Color.White)
        }
    }
}

@Composable
fun RegisterSection(nc: NavController){
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 7.5.dp, end = 7.5.dp)
            .fillMaxWidth()
    ){
        Text(
            text = "Don't have an account?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {nc.navigate("registration")},
            colors = buttonColours(),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .width(300.dp)
        ) {
            Text(text = "Sign Up", fontSize = 25.sp, color = Color.White)
        }
    }
}