package io.github.nailik.androidresampler_demo.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import io.github.nailik.androidresampler_demo.Configuration
import io.github.nailik.androidresampler_demo.bufferSizeFactor

object AudioTrackFactory {


    fun getInputAudioTrack(configuration: Configuration): AudioTrack {
        with(configuration) {
            return AudioTrack(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build(),
                AudioFormat.Builder()
                    .setSampleRate(audioInputSampleRateType.value)
                    .setChannelMask(audioInputChannelType.output)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .build(),
                AudioRecorderFactory.getInputBufferSize(configuration),
                AudioTrack.MODE_STREAM,
                0
            )
        }
    }

    fun getOutputAudioTrack(configuration: Configuration): AudioTrack {
        with(configuration) {
            val bufferSize = AudioRecord.getMinBufferSize(
                audioOutputSampleRateType.value,
                audioOutputChannelType.input,
                AudioFormat.ENCODING_PCM_16BIT
            ) * bufferSizeFactor

            return AudioTrack(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build(),
                AudioFormat.Builder()
                    .setSampleRate(audioOutputSampleRateType.value)
                    .setChannelMask(audioOutputChannelType.output)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .build(),
                bufferSize,
                AudioTrack.MODE_STREAM,
                0
            )
        }
    }

}