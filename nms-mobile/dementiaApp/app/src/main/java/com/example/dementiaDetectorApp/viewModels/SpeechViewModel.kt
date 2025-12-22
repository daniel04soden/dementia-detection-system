package com.example.dementiaDetectorApp.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.frazo.audio_services.recorder.AndroidAudioRecorder
import br.com.frazo.audio_services.recorder.AudioRecordingData
import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.api.tests.TestRepository
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
    private val testRepository: TestRepository
) : ViewModel() {

    private val audioRecorder by lazy { AndroidAudioRecorder(context, dispatchers.Default) }

    private val resultChannel = Channel<TestResult<Unit>>()
    val testResults = resultChannel.receiveAsFlow()

    private val _prefaceVisi = MutableStateFlow(true)
    val prefaceVisi: StateFlow<Boolean> = _prefaceVisi
    fun onVisiChange(newVisi: Boolean) { _prefaceVisi.value = newVisi }

    private val _recVisi = MutableStateFlow(false)
    val recVisi: StateFlow<Boolean> = _recVisi
    fun onRecChange() { _recVisi.value = !_recVisi.value }

    private val _paymentVisi = MutableStateFlow(false)
    val paymentVisi: StateFlow<Boolean> = _paymentVisi
    fun isPaymentReq(paid: Boolean) { _paymentVisi.value = paid }

    private val _successVisi = MutableStateFlow(false)
    val successVisi: StateFlow<Boolean> = _successVisi
    fun onSuccess() { _successVisi.value = !_successVisi.value }

    private val _img = MutableStateFlow(R.drawable.beach)
    val img: StateFlow<Int> = _img
    fun OnImgChange() {
        _img.value = when (_img.value) {
            R.drawable.beach -> R.drawable.desert
            R.drawable.desert -> R.drawable.forrest
            R.drawable.forrest -> R.drawable.mountains
            else -> R.drawable.beach
        }
    }

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
            _audioRecordFlow.value = emptyList()
            currentAudioFile?.delete()
            _audioNoteStatus.value = AudioNoteStatus.RECORDING

            val audioDirectory = File(
                context.getExternalFilesDir(null) ?: context.filesDir,
                "recordings"
            ).apply { mkdirs() }

            currentAudioFile = File(audioDirectory, "${UUID.randomUUID()}.wav")

            currentAudioFile?.let { outputFile ->
                recordingJob = launch(dispatchers.Default) {
                    delay(30_000L)
                    stopRecording()
                }

                val flow = audioRecorder.startRecording(outputFile)
                flow
                    .catch { e ->
                        stopRecording()
                        resultChannel.trySend(TestResult.UnknownError())
                    }
                    .collectLatest { data ->
                        val currentList = _audioRecordFlow.value
                        _audioRecordFlow.value = if (currentList.size >= 1000) {
                            currentList.drop(1) + data
                        } else {
                            currentList + data
                        }
                    }
            }
        }
    }

    fun stopRecording() {
        recordingJob?.cancel()
        recordingJob = null
        audioRecorder.stopRecording()
        currentAudioFile?.let {
            _audioNoteStatus.value = AudioNoteStatus.CAN_PLAY
            resultChannel.trySend(TestResult.Success(Unit))
        }
    }

    fun resetRecording() {
        stopRecording()
        _audioRecordFlow.value = emptyList()
        currentAudioFile?.delete()
        currentAudioFile = null
        _audioNoteStatus.value = AudioNoteStatus.HAVE_TO_RECORD
    }

    fun uploadAudioFile() {
        val file = currentAudioFile
        if (file == null) {
            Log.d("TestUploadAudio", "No audio file to upload")
            return
        }

        Log.d("TestUploadAudio", "Starting upload for file: ${file.name}")

        viewModelScope.launch(dispatchers.IO) {
            try {
                val result = testRepository.uploadAudio(file)
                Log.d("TestUploadAudio", "Upload result: $result")
                resultChannel.trySend(result)
            } catch (e: Exception) {
                Log.e("TestUploadAudio", "Upload failed", e)
            }
        }
    }

    override fun onCleared() {
        recordingJob?.cancel()
        super.onCleared()
    }
}