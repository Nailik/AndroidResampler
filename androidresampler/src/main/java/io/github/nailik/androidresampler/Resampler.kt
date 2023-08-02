package io.github.nailik.androidresampler

import io.github.nailik.androidresampler.processor.AudioProcessorFactory

class Resampler(
    configuration: ResamplerConfiguration
) {

    var isDisposed = false
        private set

    private val audioProcessor = AudioProcessorFactory.createAudioProcessor(configuration)

    fun resample(inputData: ByteArray): ByteArray {
        return audioProcessor.processData(inputData)
    }

    fun dispose() {
        isDisposed = true
        audioProcessor.release()
    }

}