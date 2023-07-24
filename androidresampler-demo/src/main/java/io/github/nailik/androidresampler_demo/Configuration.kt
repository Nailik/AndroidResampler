package io.github.nailik.androidresampler_demo

import io.github.nailik.androidresampler.ResamplerConfiguration
import io.github.nailik.androidresampler.data.ResamplerQuality
import io.github.nailik.androidresampler_demo.data.AudioRecorderChannelType
import io.github.nailik.androidresampler_demo.data.AudioRecorderSampleRate
import kotlinx.collections.immutable.toImmutableList

const val bufferSizeFactor = 2

data class Configuration(
    val quality: ResamplerQuality = ResamplerQuality.BEST,
    val audioInputChannelType: AudioRecorderChannelType = AudioRecorderChannelType.Mono,
    val audioInputSampleRateType: AudioRecorderSampleRate = AudioRecorderSampleRate.SR44100,
    val audioOutputChannelType: AudioRecorderChannelType = AudioRecorderChannelType.Mono,
    val audioOutputSampleRateType: AudioRecorderSampleRate = AudioRecorderSampleRate.SR16000,
) {

    val qualityOptions = ResamplerQuality.values().toList().toImmutableList()
    val channelTypeOptions = AudioRecorderChannelType.values().toList().toImmutableList()
    val sampleRateOptions = AudioRecorderSampleRate.values().toList().toImmutableList()

    fun toResamplerConfiguration(): ResamplerConfiguration {
        return ResamplerConfiguration(
            quality = quality,
            inputChannel = audioInputChannelType.resamplerChannel,
            inputSampleRate = audioInputSampleRateType.value,
            outputChannel = audioOutputChannelType.resamplerChannel,
            outputSampleRate = audioOutputSampleRateType.value
        )
    }

}