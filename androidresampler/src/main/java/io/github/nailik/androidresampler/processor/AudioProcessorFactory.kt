/*
 * Copyright 2022 LinkedIn Corporation
 * All Rights Reserved.
 *
 * Licensed under the BSD 2-Clause License (the "License").  See License in the project root for
 * license information.
 */
package io.github.nailik.androidresampler.processor

import io.github.nailik.androidresampler.ResamplerConfiguration

internal object AudioProcessorFactory {

    fun createAudioProcessor(
        configuration: ResamplerConfiguration
    ): AudioProcessor {
        return if (configuration.inputIsOutput()) {
            PassthroughAudioProcessor()
        } else {
            OboeAudioProcessor(configuration)
        }
    }

    private fun ResamplerConfiguration.inputIsOutput(): Boolean {
        return inputChannel == outputChannel &&
                inputSampleRate == outputSampleRate
    }

}