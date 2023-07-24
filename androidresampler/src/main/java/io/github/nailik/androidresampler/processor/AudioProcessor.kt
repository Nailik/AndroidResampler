/*
 * Copyright 2022 LinkedIn Corporation
 * All Rights Reserved.
 *
 * Licensed under the BSD 2-Clause License (the "License").  See License in the project root for
 * license information.
 */
package io.github.nailik.androidresampler.processor

internal interface AudioProcessor {

    fun processData(sourceData: ByteArray): ByteArray

    fun release()

}
