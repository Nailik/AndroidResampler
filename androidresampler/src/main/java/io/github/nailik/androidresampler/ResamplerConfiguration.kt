package io.github.nailik.androidresampler

import io.github.nailik.androidresampler.data.ResamplerChannel
import io.github.nailik.androidresampler.data.ResamplerQuality

const val BYTES_PER_SAMPLE = 2

data class ResamplerConfiguration(
    val quality: ResamplerQuality,
    val inputChannel: ResamplerChannel,
    val inputSampleRate: Int,
    val outputChannel: ResamplerChannel,
    val outputSampleRate: Int
)