package io.github.nailik.androidresampler_demo.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioRecord
import androidx.core.app.ActivityCompat
import io.github.nailik.androidresampler.Resampler
import io.github.nailik.androidresampler_demo.Configuration
import io.github.nailik.androidresampler_demo.audio.AudioRecorderFactory
import io.github.nailik.androidresampler_demo.audio.AudioTrackFactory
import io.github.nailik.androidresampler_demo.viewmodel.UpdateConfigurationEvent.SelectInputChannelType
import io.github.nailik.androidresampler_demo.viewmodel.UpdateConfigurationEvent.SelectInputSampleRate
import io.github.nailik.androidresampler_demo.viewmodel.UpdateConfigurationEvent.SelectOutputChannelType
import io.github.nailik.androidresampler_demo.viewmodel.UpdateConfigurationEvent.SelectOutputSampleRate
import io.github.nailik.androidresampler_demo.viewmodel.UpdateConfigurationEvent.SelectQuality
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * recordedData and convertedData is stored for playback later on
 *
 * recordedData is send to recording Shared flow which is consumed and then converted
 * this ensures that recording is not blocked while converting audio and also that
 * audio is converted in order
 */
class MainViewModel {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _state = MutableStateFlow(
        UiState(
            configuration = Configuration(),
            isRecording = false,
            chunksRecorded = 0,
            chunksConverted = 0
        )
    )
    val state: StateFlow<UiState> = _state

    private var audioRecorder: AudioRecord? = null
    private var resampler = Resampler(state.value.configuration.toResamplerConfiguration())

    private var inputAudioTrack = AudioTrackFactory.getInputAudioTrack(state.value.configuration)
    private var outputAudioTrack = AudioTrackFactory.getOutputAudioTrack(state.value.configuration)

    private val recording = MutableSharedFlow<ByteArray>()
    private var convertJob: Job? = null

    private var recordedData = ByteArray(0)
    private var convertedData = ByteArray(0)

    fun updateConfiguration(event: UpdateConfigurationEvent) {
        inputAudioTrack.stop()
        outputAudioTrack.stop()
        _state.update {
            val configuration = when (event) {
                is SelectInputChannelType -> it.configuration.copy(audioInputChannelType = event.channelType)
                is SelectInputSampleRate -> it.configuration.copy(audioInputSampleRateType = event.sampleRate)
                is SelectOutputChannelType -> it.configuration.copy(audioOutputChannelType = event.channelType)
                is SelectOutputSampleRate -> it.configuration.copy(audioOutputSampleRateType = event.sampleRate)
                is SelectQuality -> it.configuration.copy(quality = event.quality)
            }
            it.copy(configuration = configuration)
        }
    }

    /**
     * record audio and send data to recording flow
     */
    fun recordAudio(context: Context) {
        if (audioRecorder?.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
            audioRecorder?.stop()
            return
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        recordedData = ByteArray(0)
        convertedData = ByteArray(0)

        inputAudioTrack.stop()
        outputAudioTrack.stop()

        _state.update { it.copy(isRecording = true, chunksRecorded = 0, chunksConverted = 0) }

        startConvertJob()

        audioRecorder = AudioRecorderFactory.getRecorder(state.value.configuration).apply {
            startRecording()

            coroutineScope.launch {
                val bufferSize = AudioRecorderFactory.getInputBufferSize(_state.value.configuration)
                val byteArray = ByteArray(bufferSize)
                while (recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                    if (read(byteArray, 0, byteArray.size) >= bufferSize) {

                        recordedData += byteArray
                        recording.emit(byteArray)

                        _state.update { it.copy(chunksRecorded = it.chunksRecorded + 1) }
                    }
                }
                _state.update { it.copy(isRecording = false) }
            }

        }
    }

    /**
     * start audio convert job and cancel previous
     */
    private fun startConvertJob() {
        resampler.dispose()
        resampler = Resampler(state.value.configuration.toResamplerConfiguration())
        convertJob?.cancel()

        convertJob = coroutineScope.launch {
            recording.collect { data ->
                convertedData += resampler.resample(data)

                _state.update { it.copy(chunksRecorded = it.chunksConverted + 1) }
            }
        }

    }

    fun playOriginalAudio() {
        coroutineScope.launch {
            inputAudioTrack = AudioTrackFactory.getInputAudioTrack(state.value.configuration)

            inputAudioTrack.stop()
            outputAudioTrack.stop()

            inputAudioTrack.play()

            inputAudioTrack.write(recordedData, 0, recordedData.size)
        }
    }

    fun playConvertedAudio() {
        coroutineScope.launch {
            outputAudioTrack = AudioTrackFactory.getOutputAudioTrack(state.value.configuration)

            inputAudioTrack.stop()
            outputAudioTrack.stop()

            outputAudioTrack.play()

            outputAudioTrack.write(convertedData, 0, convertedData.size)
        }
    }

}