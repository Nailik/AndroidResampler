package io.github.nailik.androidresampler_demo.data

import android.media.AudioFormat
import io.github.nailik.androidresampler.data.ResamplerChannel

enum class AudioRecorderChannelType(
    val input: Int,
    val output: Int,
    val resamplerChannel: ResamplerChannel
) {
    Mono(AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_OUT_MONO, ResamplerChannel.MONO),
    Stereo(AudioFormat.CHANNEL_IN_STEREO, AudioFormat.CHANNEL_OUT_STEREO, ResamplerChannel.STEREO);
}