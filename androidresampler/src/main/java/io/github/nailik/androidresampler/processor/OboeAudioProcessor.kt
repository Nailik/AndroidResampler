package io.github.nailik.androidresampler.processor

import io.github.nailik.androidresampler.BYTES_PER_SAMPLE
import io.github.nailik.androidresampler.ResamplerConfiguration
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.ceil
import kotlin.math.min

/**
 * Implementation of audio processor that uses Oboe library
 */
internal class OboeAudioProcessor(
    private val configuration: ResamplerConfiguration,
) : AudioProcessor {

    init {
        with(configuration) {
            initProcessor(
                quality = quality.ordinal,
                sourceChannelCount = inputChannel.count,
                sourceSampleRate = inputSampleRate,
                targetChannelCount = outputChannel.count,
                targetSampleRate = outputSampleRate
            )
        }
    }

    override fun processData(sourceData: ByteArray): ByteArray {

        val sourceSampleCount =
            sourceData.size / (BYTES_PER_SAMPLE * configuration.inputChannel.count)

        val samplingRatio =
            configuration.outputSampleRate.toDouble() / configuration.inputSampleRate.toDouble()
        val estimatedTargetSampleCount = ceil(sourceSampleCount * samplingRatio)

        //adding 10 percent on top in case estimate is too low
        val targetBufferCapacity =
            (estimatedTargetSampleCount * configuration.outputChannel.count * BYTES_PER_SAMPLE * 1.1).toInt()

        val sourceBuffer = ByteBuffer.allocateDirect(sourceData.size).order(ByteOrder.LITTLE_ENDIAN)
        sourceBuffer.put(sourceData)

        val targetBuffer =
            ByteBuffer.allocateDirect(targetBufferCapacity).order(ByteOrder.LITTLE_ENDIAN)

        val targetSampleCount = processAudioFrame(sourceBuffer, sourceSampleCount, targetBuffer, targetBufferCapacity)
        val targetBufferSize =
            targetSampleCount * BYTES_PER_SAMPLE * configuration.outputChannel.count

        //limit to resulting targetBufferSize only if it's smaller than the initial targetBufferCapacity
        val limit = min(targetBufferSize, targetBufferCapacity)

        targetBuffer.rewind()
        targetBuffer.limit(limit)
        targetBuffer.position(0)

        val targetData = ByteArray(limit)
        targetBuffer.get(targetData)

        return targetData
    }

    override fun release() {
        releaseProcessor()
    }

    private external fun initProcessor(
        quality: Int,
        sourceChannelCount: Int,
        sourceSampleRate: Int,
        targetChannelCount: Int,
        targetSampleRate: Int
    )

    private external fun processAudioFrame(
        sourceBuffer: ByteBuffer,
        sampleCount: Int,
        targetBuffer: ByteBuffer,
        targetBufferCapacity: Int
    ): Int

    private external fun releaseProcessor()

    companion object {
        init {
            System.loadLibrary("androidresampler")
        }
    }
}