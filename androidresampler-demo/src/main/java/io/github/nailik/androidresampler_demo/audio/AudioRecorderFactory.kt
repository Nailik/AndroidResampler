package io.github.nailik.androidresampler_demo.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import io.github.nailik.androidresampler_demo.Configuration
import io.github.nailik.androidresampler_demo.bufferSizeFactor

object AudioRecorderFactory {

    fun getInputBufferSize(configuration: Configuration): Int {
        with(configuration) {
            return AudioRecord.getMinBufferSize(
                audioInputSampleRateType.value,
                audioInputChannelType.input,
                AudioFormat.ENCODING_PCM_16BIT
            ) * bufferSizeFactor
        }
    }

    @SuppressLint("MissingPermission")
    fun getRecorder(configuration: Configuration): AudioRecord {
        with(configuration) {
            return AudioRecord.Builder()
                .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setSampleRate(audioInputSampleRateType.value)
                        .setChannelMask(audioInputChannelType.input)
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .build()
                )
                .setBufferSizeInBytes(getInputBufferSize(configuration))
                .build()
        }
    }

}