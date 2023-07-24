/*
 * Copyright 2022 LinkedIn Corporation
 * All Rights Reserved.
 *
 * Licensed under the BSD 2-Clause License (the "License").  See License in the project root for
 * license information.
 */
package io.github.nailik.androidresampler.processor

/**
 * Simple implementation of [AudioProcessor] that duplicates a source frame into target frame.
 */
internal class PassthroughAudioProcessor : AudioProcessor {

    override fun processData(sourceData: ByteArray): ByteArray {
        return sourceData
    }

    override fun release() {}

}