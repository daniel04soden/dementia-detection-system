package com.example.dementiaDetectorApp.ui.views

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.models.PaymentState
import com.example.dementiaDetectorApp.ui.composables.Footer
import com.example.dementiaDetectorApp.ui.composables.StripePaymentButton
import com.example.dementiaDetectorApp.ui.theme.Gray
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.ui.theme.buttonColours
import com.example.dementiaDetectorApp.viewModels.PaymentVM
import com.example.dementiaDetectorApp.viewModels.QViewModel
import com.example.dementiaDetectorApp.viewModels.SharedVM
import com.zekierciyas.library.view.SurveyScreen

@Composable
fun QuestionnaireScreen(qVM: QViewModel, pVM: PaymentVM, sharedVM: SharedVM, nc: NavController){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MidPurple)
    ){
        Column(
            Modifier.padding(bottom = 50.dp)
        ){
            if(qVM.prefaceVisi.collectAsState().value){
                Spacer(Modifier.height(70.dp))
            }
            else{
                Spacer(Modifier.height(35.dp))
            }
            PrefaceSection(qVM)
            FormSection(qVM,sharedVM,nc)
            //PaymentPrompt(qVM, sharedVM, pVM, nc)
            SuccessSection(qVM,nc)
        }
        if(!(qVM.prefaceVisi.collectAsState().value)){
            Footer(Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun PrefaceSection(qVM: QViewModel){
    AnimatedVisibility(
        visible =qVM.prefaceVisi.collectAsState().value,
        exit = slideOutHorizontally() + fadeOut()
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text="Lifestyle Questionnaire",
                    textAlign = TextAlign.Center,
                    fontSize = 32.5.sp,
                    lineHeight = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                HorizontalDivider(
                    thickness = 2.dp,
                    color = Color.White,
                )
                Column(modifier = Modifier
                    .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(125.dp)
                ){
                    Text(
                        text = "The following is a questionnaire covering general questions, habits and certain readings that we recommend you get from your GP\n\n" +
                                "Although not 100% accurate this questionnaire can help predict the presence of dementia",
                        fontSize = 20.sp,
                        color = Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 35.dp, start = 15.dp, end = 15.dp)
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(0.75F),
                        colors = buttonColours(),
                        onClick = {
                            qVM.onVisiChange(false)
                            qVM.onS1Change(true)}
                    ){
                        Text(
                            text = "Take Questionnaire",
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FormSection(qVM: QViewModel, sharedVM: SharedVM, nc: NavController){
    AnimatedVisibility(
        visible = !(qVM.prefaceVisi.collectAsState().value),
    ){
        Column{
            AnimatedVisibility(
                visible = qVM.s1visi.collectAsState().value,
            ){
                S1Section(qVM)
            }
            AnimatedVisibility(
                visible = qVM.s2visi.collectAsState().value,
            ){
                S2Section(qVM)
            }
            AnimatedVisibility(
                visible = qVM.s3visi.collectAsState().value,
            ){
                S3Section(qVM, sharedVM)
            }
        }
    }
}

@Composable
private fun S1Section(qVM: QViewModel){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(Modifier.height(35.dp))
        Row(modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text( "General Questions", fontSize = 35.sp, fontWeight = FontWeight.Bold, color = MidPurple)
        }
        Row(Modifier.fillMaxHeight(0.9F)){
            SurveyScreen(
                survey = qVM.s1Survey,
                callbackAnswers = {answers ->
                    qVM.onSurveyAnswerChange(answers)
                }
            )
        }
        Button(
            onClick = {
                qVM.onS1Change(false)
                qVM.onS2Change(true)
            },
            enabled = qVM.s1Complete.value,
            colors = buttonColours(),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.75F)
        ){
            Text(
                text = "Next Questions",
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun S2Section(qVM: QViewModel){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(Modifier.height(35.dp))
        Row(modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text( "GP + Medication", fontSize = 35.sp, fontWeight = FontWeight.Bold, color = MidPurple)
        }

        Row(Modifier.fillMaxHeight(0.9F)){
            SurveyScreen(
                survey = qVM.s2Survey,
                callbackAnswers = {answers ->
                    qVM.onSurveyAnswerChange(answers)
                }
            )
        }
        Button(
            onClick = {
                qVM.onS2Change(false)
                qVM.onS3Change(true)
            },
            enabled = qVM.s2Complete.value,
            colors = buttonColours(),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.75F)
        ){
            Text(
                text = "Next Questions",
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun S3Section(qVM: QViewModel, sharedVM: SharedVM){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(Modifier.height(35.dp))
        Row(modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text( "Habbit Questions", fontSize = 35.sp, fontWeight = FontWeight.Bold, color = MidPurple)
        }
        Row(Modifier.fillMaxHeight(0.9F)){
            SurveyScreen(
                survey = qVM.s3Survey,
                callbackAnswers = {answers ->
                    qVM.onSurveyAnswerChange(answers)
                }
            )
        }
        Button(
            /*onClick = {
                Log.d("BTN", "PRESSED")
                if (!sharedVM.hasPaid.value) qVM.isPaymentReq(true) else qVM.onSuccessChange(true)
                qVM.onS3Change(false)
            },*/
            onClick = {
                qVM.onS3Change(false)
                qVM.submitAnswers(sharedVM.id.value)
                sharedVM.onTestSubmission(0)
                qVM.onSuccessChange(true)
                      },
            enabled = qVM.s3Complete.value,
            colors = buttonColours(),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.75F)
        ){
            Text(
                text = "Continue",
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun PaymentPrompt(
    qVM: QViewModel,
    sharedVM: SharedVM,
    pVM: PaymentVM,
    nc: NavController
) {
    val paymentState by pVM.paymentState.collectAsState()

    val context = LocalContext.current

    AnimatedVisibility(
        visible = qVM.paymentVisi.collectAsState().value,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(MidPurple).padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Spacer(Modifier.height(35.dp))
            Text("AI Features", fontSize = 32.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
            HorizontalDivider(thickness = 2.dp, color = Color.White)

            Text(
                text = "Our questionnaire grading relies on the use of our AI model for returning our results.\n\nUnfortunately we cannot run said model for free and as such we require a payment from each user to access its features.",
                color = Color.White,
                fontSize = 20.sp
            )

            Text(
                text = "By paying €5, you gain access to the use of our AI for grading the Lifestyle-Questionnaire and the Speech Test\n\nUsing the AI will get you an immediate response with a high but not perfect level of accuracy",
                color = Color.White,
                fontSize = 18.sp
            )

            StripePaymentButton(pVM, sharedVM) {
                Text(
                    text = "Use the AI\n(Requires Premium)",
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = {nc.navigate("home")},
                colors = buttonColours(),
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Text("No thanks\n(Don't submit test)", fontSize = 20.sp, color = Color.White)
            }
        }
    }
    // Wrap onSuccess in LaunchedEffect to avoid calling it during recomposition
    if (paymentState is PaymentState.Success) {
        LaunchedEffect(Unit) {
            qVM.isPaymentReq(false)
            qVM.onSuccessChange(true)
        }
    }
}

@Composable
private fun SuccessSection(qVM: QViewModel, nc: NavController) {
    AnimatedVisibility(
        visible = qVM.successVisi.collectAsState().value,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(MidPurple).padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(35.dp)
        ) {
            Spacer(Modifier.height(35.dp))
            Text("Questionnaire Complete", fontSize = 32.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
            HorizontalDivider(thickness = 2.dp, color = Color.White)
            Text(
                text = "Your anwsers have been submitted\n\nA medical professional will look over them shortly",
                color = Color.White,
                fontSize = 20.sp
            )
            Button(onClick = {nc.navigate("home")}, colors = buttonColours(), modifier = Modifier.width(300.dp)) {
                Text("Return to Home", fontSize = 25.sp, color = Color.White)
            }
        }
    }
}

@Composable
private fun AISuccessSection(qVM: QViewModel, sharedVM: SharedVM, nc: NavController) {
    AnimatedVisibility(
        visible = qVM.aiSuccessVisi.collectAsState().value,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(MidPurple).padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(35.dp)
        ) {
            Spacer(Modifier.height(35.dp))
            Text("Questionnaire Complete", fontSize = 32.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
            HorizontalDivider(thickness = 2.dp, color = Color.White)
            Text(
                text = "Would you like to submit the questionnaire you have just taken for analysis, or would you like to submit it later?",
                color = Color.White,
                fontSize = 20.sp
            )
            Button(onClick = {
                //LAST REPO CALL HERE
                sharedVM.onTestSubmission(0)
                             }, colors = buttonColours(), modifier = Modifier.width(300.dp)) {
                Text("Submit Answers", fontSize = 25.sp, color = Color.White)
            }
            Button(
                onClick = {
                    nc.navigate("home")
                },
                colors = buttonColours(),
                modifier = Modifier.width(300.dp)
            ) {
                Text("Cancel", fontSize = 25.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun SubmitQuestionnaireScreen(qVM: QViewModel, pVM: PaymentVM, sharedVM: SharedVM, nc: NavController){
    qVM.isPaymentReq(true)
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MidPurple)
    ){
        AISuccessSection(qVM, sharedVM, nc)
        PaymentPrompt(qVM, sharedVM, pVM, nc)
    }
}