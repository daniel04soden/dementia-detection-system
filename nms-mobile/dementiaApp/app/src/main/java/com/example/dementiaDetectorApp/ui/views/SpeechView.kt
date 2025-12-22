package com.example.dementiaDetectorApp.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.frazo.audio_services.recorder.AudioRecordingData
import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.models.PaymentState
import com.example.dementiaDetectorApp.ui.composables.Footer
import com.example.dementiaDetectorApp.ui.composables.StripePaymentButton
import com.example.dementiaDetectorApp.ui.theme.DarkPurple
import com.example.dementiaDetectorApp.ui.theme.Gray
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.ui.theme.buttonColours
import com.example.dementiaDetectorApp.viewModels.SharedVM
import com.example.dementiaDetectorApp.viewModels.SpeechViewModel
import com.example.dementiaDetectorApp.viewModels.PaymentVM
import kotlin.math.max

@Composable
fun SpeechScreen(
    sVM: SpeechViewModel,
    sharedVM: SharedVM,
    pVM: PaymentVM,
    nc: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidPurple)
    ) {
        Column(Modifier.padding(bottom = 50.dp)) {
            if (sVM.prefaceVisi.collectAsState().value) {
                Spacer(Modifier.height(70.dp))
            } else {
                Spacer(Modifier.height(35.dp))
            }
            PrefaceSection(sVM)
            TestSection(sVM, sharedVM, nc)
            PaymentPrompt(sVM, sharedVM, pVM, nc)
            SuccessSection(sVM, sharedVM, nc)
        }
        if (!sVM.paymentVisi.collectAsState().value) {
            Footer(Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun PrefaceSection(sVM: SpeechViewModel) {
    AnimatedVisibility(
        visible = sVM.prefaceVisi.collectAsState().value,
        exit = slideOutHorizontally() + fadeOut()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Speech Test",
                    textAlign = TextAlign.Center,
                    fontSize = 32.5.sp,
                    lineHeight = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                HorizontalDivider(thickness = 2.dp, color = Color.White)
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(125.dp)
                ) {
                    Text(
                        text = "The following is a test that will take a recording of your voice\n\nThe test will begin by displaying an image on the screen that you must then describe\n\nThe recording will last 30 seconds, please try to keep speaking for the entirety of the test, if needed there is a button for displaying a new image to allow you to say more",
                        fontSize = 20.sp,
                        color = Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 35.dp, start = 15.dp, end = 15.dp)
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(0.75F),
                        colors = buttonColours(),
                        onClick = {
                            sVM.onVisiChange(false)
                            sVM.onRecChange()
                        }
                    ) {
                        Text(text = "Continue", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun TestSection(sVM: SpeechViewModel, sharedVM: SharedVM, nc: NavController) {
    AnimatedVisibility(
        visible = sVM.recVisi.collectAsState().value,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(35.dp)
        ) {
            HeaderSection(sVM)
            ImageSection(sVM)
            RecordingSection(sVM, sharedVM, nc)
        }
    }
}

@Composable
private fun HeaderSection(sVM: SpeechViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Speech Test",
            fontSize = 32.5.sp,
            color = DarkPurple,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ImageSection(sVM: SpeechViewModel) {
    val img = sVM.img.collectAsState().value
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Image(
            painter = painterResource(img),
            contentDescription = "Image to describe",
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            contentScale = ContentScale.FillBounds
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { sVM.OnImgChange() },
                enabled = sVM.audioStatus.collectAsState().value == SpeechViewModel.AudioNoteStatus.RECORDING,
                colors = buttonColours(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .width(300.dp)
                    .padding(bottom = 25.dp)
            ) {
                Text(text = "Next Image", fontSize = 25.sp, color = Color.White)
            }
        }
    }
}

@Composable
private fun RecordingSection(sVM: SpeechViewModel, sharedVM: SharedVM, nc: NavController) {
    val audioStatus by sVM.audioStatus.collectAsState()
    val audioRecordingData by sVM.audioRecordFlow.collectAsState()
    val latestData = audioRecordingData.lastOrNull()
    val elapsed = (latestData as? AudioRecordingData.Recording)?.elapsedTime ?: 0L
    val remainingSeconds = max(0, (30_000 - elapsed) / 1000)

    when (audioStatus) {
        SpeechViewModel.AudioNoteStatus.HAVE_TO_RECORD -> ReadyToRecordSection(sVM)
        SpeechViewModel.AudioNoteStatus.RECORDING -> RecordingActiveSection(audioRecordingData, remainingSeconds)
        SpeechViewModel.AudioNoteStatus.CAN_PLAY -> RecordingCompleteSection(sVM, sharedVM, nc)
    }
}

@Composable
private fun ReadyToRecordSection(sVM: SpeechViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tap to start recording", fontSize = 22.5.sp, color = DarkPurple)
        Spacer(Modifier.height(20.dp))
        AudioRecorder(
            recordIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.mic),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp),
                    tint = Color.White
                )
            },
            audioRecordingData = emptyList(),
            onRecordRequested = { sVM.startRecording() }
        )
    }
}

@Composable
private fun RecordingActiveSection(
    audioRecordingData: List<AudioRecordingData>,
    remainingSeconds: Long
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${remainingSeconds}s remaining",
            fontSize = 32.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (remainingSeconds < 5) Color.Red else DarkPurple
        )
        Spacer(Modifier.height(20.dp))
        AudioRecorder(
            recordIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.mic),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp),
                    tint = Color.White
                )
            },
            audioRecordingData = audioRecordingData,
            onRecordRequested = {}
        )
    }
}

@Composable
private fun RecordingCompleteSection(sVM: SpeechViewModel, sharedVM: SharedVM, nc: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.check_circle),
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = Color.Green
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Recording Complete!",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = DarkPurple
        )
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 7.5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { sVM.resetRecording() },
                colors = buttonColours(),
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Redo", fontSize = 20.sp, color = Color.White)
            }
            Button(
                onClick = {
                    sVM.onRecChange()
                    if (true) sVM.isPaymentReq(true) else sVM.onSuccess()
                },
                colors = buttonColours(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun AudioRecorder(
    modifier: Modifier = Modifier,
    recordIcon: @Composable () -> Unit,
    audioRecordingData: List<AudioRecordingData>,
    onRecordRequested: () -> Unit,
    totalDurationMs: Long = 30_000L
) {
    val latestData = audioRecordingData.lastOrNull()
    val elapsed = (latestData as? AudioRecordingData.Recording)?.elapsedTime ?: 0L
    val progress = (elapsed.toFloat() / totalDurationMs).coerceIn(0f, 1f)
    val isRecording = audioRecordingData.isNotEmpty()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = isRecording) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(10.dp)
                    .background(Color.Red.copy(alpha = 0.2f), RoundedCornerShape(5.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(Color.Red, RoundedCornerShape(5.dp))
                )
            }
        }

        Button(
            onClick = onRecordRequested,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRecording) Color.Red else buttonColours().containerColor
            ),
            shape = RoundedCornerShape(70.dp),
            modifier = Modifier.fillMaxWidth(0.5f).height(50.dp)
        ) {
            recordIcon()
        }

        Text(
            text = if (isRecording) "Recording..." else "Tap to record",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun PaymentPrompt(
    sVM: SpeechViewModel,
    sharedVM: SharedVM,
    pVM: PaymentVM,
    nc: NavController
) {
    val paymentState by pVM.paymentState.collectAsState()

    val context = LocalContext.current

    AnimatedVisibility(
        visible = sVM.paymentVisi.collectAsState().value,
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
                text = "Our speech test relies on the use of our AI model for returning our results.\n\nUnfortunately we cannot run said model for free and as such we require a payment from each user to access its features.",
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
                    text = "Use the AI\n(Pay €5)",
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
            sVM.isPaymentReq(false)
            sVM.onSuccess()
        }
    }
}

@Composable
private fun SuccessSection(sVM: SpeechViewModel, sharedVM: SharedVM, nc: NavController) {
    AnimatedVisibility(
        visible = sVM.successVisi.collectAsState().value,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(MidPurple).padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(35.dp)
        ) {
            Spacer(Modifier.height(35.dp))
            Text("Test Complete", fontSize = 32.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
            HorizontalDivider(thickness = 2.dp, color = Color.White)
            Text(
                text = "Would you like to submit the recording you have just taken for analysis, or would you like to redo it?",
                color = Color.White,
                fontSize = 20.sp
            )
            Button(onClick = { sVM.uploadAudioFile() }, colors = buttonColours(), modifier = Modifier.width(300.dp)) {
                Text("Submit Recording", fontSize = 25.sp, color = Color.White)
            }
            Button(
                onClick = {
                    sVM.onSuccess()
                    sVM.onRecChange()
                    sharedVM.onTestSubmission(3)
                },
                colors = buttonColours(),
                modifier = Modifier.width(300.dp)
            ) {
                Text("Cancel", fontSize = 25.sp, color = Color.White)
            }
        }
    }
}