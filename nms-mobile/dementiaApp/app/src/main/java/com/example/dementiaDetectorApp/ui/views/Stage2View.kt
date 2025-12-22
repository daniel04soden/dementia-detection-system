package com.example.dementiaDetectorApp.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.ui.composables.Footer
import com.example.dementiaDetectorApp.ui.composables.SubmittedSection
import com.example.dementiaDetectorApp.ui.theme.Gray
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.ui.theme.buttonColours
import com.example.dementiaDetectorApp.viewModels.SharedVM
import com.example.dementiaDetectorApp.viewModels.Stage2VM
import com.zekierciyas.library.view.SurveyScreen

@Composable
fun Stage2Screen(tVM: Stage2VM, sVM: SharedVM, nc: NavController){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MidPurple)
    ){
        Column(
            Modifier.padding(bottom = 50.dp)
        ){
            if(tVM.prefaceVisi.collectAsState().value){
                Spacer(Modifier.height(70.dp))
            }
            else{
                Spacer(Modifier.height(35.dp))
            }
            PrefaceSection(tVM)
            FormSection(tVM,sVM,nc)
        }
        if(!(tVM.prefaceVisi.collectAsState().value)){
            Footer(Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun PrefaceSection(tVM: Stage2VM){
    AnimatedVisibility(
        visible =tVM.prefaceVisi.collectAsState().value,
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
                    text="GP Cognitive Test Part 2",
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
                        text = "The following is a questionnaire to be completed by a family member or caretaker\n\n" +
                                "This other individual will be asked questions regarding your day to day life and should answer as honestly as possible",
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
                            tVM.onVisiChange(false)
                            tVM.onFormChange(true)}
                    ){
                        Text(
                            text = "Continue",
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FormSection(tVM: Stage2VM, sVM: SharedVM, nc: NavController){
    AnimatedVisibility(
        visible = !(tVM.prefaceVisi.collectAsState().value),
    ){
        Column{
            AnimatedVisibility(
                visible = tVM.formVisi.collectAsState().value,
            ){
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
                            survey = tVM.survey,
                            callbackAnswers = {answers ->
                                tVM.onSurveyAnswerChange(answers)
                            }
                        )
                    }
                    Button(
                        onClick = {
                            tVM.submitAnswers(sVM.id.value)
                        },
                        enabled = tVM.allQuestionsAnswered.collectAsState().value,
                        colors = buttonColours(),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.75F)
                    ){
                        Text(
                            text = "Subimt Answers",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = tVM.successVisi.collectAsState().value
            ){
                SubmittedSection(nc)
            }
        }
    }
}