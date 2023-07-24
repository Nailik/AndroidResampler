package io.github.nailik.androidresampler_demo.viewmodel

import io.github.nailik.androidresampler.data.ResamplerQuality
import io.github.nailik.androidresampler_demo.data.AudioRecorderChannelType
import io.github.nailik.androidresampler_demo.data.AudioRecorderSampleRate

sealed interface UpdateConfigurationEvent {

    data class SelectQuality(val quality: ResamplerQuality) : UpdateConfigurationEvent

    data class SelectInputChannelType(val channelType: AudioRecorderChannelType) :
        UpdateConfigurationEvent

    data class SelectInputSampleRate(val sampleRate: AudioRecorderSampleRate) :
        UpdateConfigurationEvent

    data class SelectOutputChannelType(val channelType: AudioRecorderChannelType) :
        UpdateConfigurationEvent

    data class SelectOutputSampleRate(val sampleRate: AudioRecorderSampleRate) :
        UpdateConfigurationEvent

}