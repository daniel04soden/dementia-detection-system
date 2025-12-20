package com.example.dementiaDetectorApp.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.frazo.audio_services.recorder.AudioRecordingData
import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.ui.composables.ProgressDots
import com.example.dementiaDetectorApp.ui.theme.DarkPurple
import com.example.dementiaDetectorApp.ui.theme.Gray
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.ui.theme.buttonColours
import com.example.dementiaDetectorApp.viewModels.SharedVM
import com.example.dementiaDetectorApp.viewModels.SpeechViewModel
import kotlin.math.max

@Composable
fun SpeechScreen(sVM: SpeechViewModel, sharedVM: SharedVM, nc: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidPurple)
    ) {
        Column(Modifier.padding(bottom = 50.dp)) {
            if (sVM.prefaceVisi.collectAsState().value) {
                Spacer(Modifier.height(120.dp))
            } else {
                Spacer(Modifier.height(35.dp))
            }
            PrefaceSection(sVM)
            TestSection(sVM, sharedVM, nc)
        }
        ProgressDots(Modifier.align(Alignment.BottomCenter))
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
                HorizontalDivider(
                    thickness = 2.dp,
                    color = Color.White,
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(125.dp)
                ) {
                    Text(
                        text = "The following is a test that will take a recording of your voice\n\n" +
                                "The test will begin by displaying an image on the screen that you must then describe\n\n" +
                                "The recording will last 30 seconds, please try to keep speaking for the entirety of the test, if needed there is a button for displaying a new image to allow you to say more",
                        fontSize = 20.sp,
                        color = Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 35.dp, start = 15.dp, end = 15.dp)
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(0.75F),
                        colors = buttonColours(),
                        onClick = { sVM.onVisiChange(false) }
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(35.dp)
    ) {
        HeaderSection(sVM)
        ImageSection(sVM)
        RecordingSection(sVM)
    }
}

@Composable
private fun HeaderSection(sVM: SpeechViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
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
                .fillMaxWidth(1f)
                .fillMaxHeight(0.5f),
            alignment = Alignment.Center,
            contentScale = ContentScale.FillBounds
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = {sVM.OnImgChange()},
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
private fun RecordingSection(sVM: SpeechViewModel) {
    val audioStatus by sVM.audioStatus.collectAsState()
    val audioRecordingData by sVM.audioRecordFlow.collectAsState()

    val latestData = audioRecordingData.lastOrNull()
    val elapsed = (latestData as? AudioRecordingData.Recording)?.elapsedTime ?: 0L
    val remainingSeconds = max(0, (30_000 - elapsed) / 1000).toLong()

    when (audioStatus) {
        SpeechViewModel.AudioNoteStatus.HAVE_TO_RECORD -> ReadyToRecordSection(sVM)
        SpeechViewModel.AudioNoteStatus.RECORDING -> RecordingActiveSection(
            audioRecordingData,
            remainingSeconds
        )
        SpeechViewModel.AudioNoteStatus.CAN_PLAY -> RecordingCompleteSection(sVM)
    }
}

@Composable
private fun ReadyToRecordSection(sVM: SpeechViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tap to start recording",
                fontSize = 22.5.sp,
                color = DarkPurple,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        AudioRecorder(
            modifier = Modifier.fillMaxWidth(),
            recordIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.mic),
                    contentDescription = "Record",
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${remainingSeconds}s remaining",
                fontSize = 32.5.sp,
                fontWeight = FontWeight.Bold,
                color = if (remainingSeconds < 5) Color.Red else DarkPurple
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        AudioRecorder(
            modifier = Modifier.fillMaxWidth(),
            recordIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.mic),
                    contentDescription = "Recording",
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
private fun RecordingCompleteSection(sVM: SpeechViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.check_circle),
                contentDescription = "Complete",
                modifier = Modifier.size(60.dp),
                tint = Color.Green
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Recording Complete!",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = DarkPurple
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 7.5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { sVM.resetRecording() },
                colors = buttonColours(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 25.dp)
                    .padding(horizontal = 7.5.dp)
            ) {
                Text(
                    text = "Redo",
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = {},
                colors = buttonColours(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp)
                    .padding(horizontal = 7.5.dp)
            ) {
                Text(
                    text = "Submit Recording",
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        AnimatedVisibility(
            visible = isRecording,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Progress bar
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
        }

        // Shows that the mic is in use
        Button(
            onClick = onRecordRequested,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRecording) Color.Red else buttonColours().containerColor
            ),
            shape = RoundedCornerShape(70.dp),
            modifier = Modifier
                .fillMaxWidth(0.5F)
                .height(50.dp)
        ) {
            recordIcon()
        }

        val statusText = when {
            isRecording -> "Recording..."
            else -> "Tap to record"
        }
        Text(
            text = statusText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}