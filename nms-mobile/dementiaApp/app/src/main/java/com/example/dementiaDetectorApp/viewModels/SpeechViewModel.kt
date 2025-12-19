package com.example.dementiaDetectorApp.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.frazo.audio_services.recorder.AndroidAudioRecorder
import br.com.frazo.audio_services.recorder.AudioRecordingData
import com.example.dementiaDetectorApp.api.tests.TestResult
import com.example.dementiaDetectorApp.di.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

@HiltViewModel
class SpeechViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: DispatcherProvider,
) : ViewModel() {
    private val audioRecorder by lazy{AndroidAudioRecorder(context, dispatchers.Default)}

    private val resultChannel = Channel<TestResult<Unit>>()
    val testResults = resultChannel.receiveAsFlow()

    private var isLoading = false

    private val _prefaceVisi = MutableStateFlow(true)
    val prefaceVisi: StateFlow<Boolean> = _prefaceVisi
    fun onVisiChange(newVisi: Boolean) { _prefaceVisi.value = newVisi }

    enum class AudioNoteStatus { HAVE_TO_RECORD, RECORDING, CAN_PLAY }

    private val _audioRecordFlow = MutableStateFlow<List<AudioRecordingData>>(emptyList())
    val audioRecordFlow = _audioRecordFlow.asStateFlow()

    private val _audioNoteStatus = MutableStateFlow(AudioNoteStatus.HAVE_TO_RECORD)
    val audioStatus = _audioNoteStatus.asStateFlow()

    private var currentAudioFile: File? = null

    private var recordingJob: Job? = null

    fun getRecordedFile(): File? = currentAudioFile

    fun startRecording() {
        viewModelScope.launch(dispatchers.IO) {
            Log.d("SpeechVM", "startRecording called")
            _audioRecordFlow.value = emptyList()
            currentAudioFile?.delete()
            _audioNoteStatus.value = AudioNoteStatus.RECORDING
            Log.d("SpeechVM", "Status changed to RECORDING")

            val audioDirectory = File(
                context.getExternalFilesDir(null) ?: context.filesDir,
                "recordings"
            ).apply { mkdirs() }
            Log.d("SpeechVM", "Directory: ${audioDirectory.absolutePath}")

            currentAudioFile = File(audioDirectory, "${UUID.randomUUID()}.wav")
            Log.d("SpeechVM", "Output file: ${currentAudioFile?.absolutePath}")

            currentAudioFile?.let { outputFile ->
                recordingJob = launch(dispatchers.Default) {
                    delay(30_000L)
                    stopRecording()
                }

                val flow = audioRecorder.startRecording(outputFile)
                flow
                    .catch { e ->
                        Log.e("SpeechVM", "Recording error", e)
                        stopRecording()
                        resultChannel.trySend(TestResult.UnknownError())
                    }
                    .collectLatest { data ->
                        val currentList = _audioRecordFlow.value
                        if (currentList.size >= 1000) {
                            _audioRecordFlow.value = currentList.drop(1)
                        }
                        _audioRecordFlow.value = currentList + data
                    }
            }
        }
    }

    fun stopRecording() {
        Log.d("SpeechVM", "stopRecording called")
        recordingJob?.cancel()
        recordingJob = null
        audioRecorder.stopRecording()
        currentAudioFile?.let {
            Log.d("SpeechVM", "Recording saved: ${it.absolutePath}")
            _audioNoteStatus.value = AudioNoteStatus.CAN_PLAY
            resultChannel.trySend(TestResult.Success(Unit))
        }
    }

    fun resetRecording() {
        Log.d("SpeechVM", "resetRecording called")
        stopRecording()
        _audioRecordFlow.value = emptyList()
        currentAudioFile?.delete()
        currentAudioFile = null
        _audioNoteStatus.value = AudioNoteStatus.HAVE_TO_RECORD
        _prefaceVisi.value = true
    }

    override fun onCleared() {
        recordingJob?.cancel()
        super.onCleared()
    }
}