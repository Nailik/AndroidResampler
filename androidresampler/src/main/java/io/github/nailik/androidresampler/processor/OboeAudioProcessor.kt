package io.github.nailik.androidresampler.processor

import io.github.nailik.androidresampler.BYTES_PER_SAMPLE
import io.github.nailik.androidresampler.ResamplerConfiguration
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.ceil

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
        val estimatedTargetSampleCount = ceil(sourceSampleCount * samplingRatio).toInt()
        val targetBufferCapacity =
            estimatedTargetSampleCount * configuration.outputChannel.count * BYTES_PER_SAMPLE

        val sourceBuffer = ByteBuffer.allocateDirect(sourceData.size).order(ByteOrder.LITTLE_ENDIAN)
        sourceBuffer.put(sourceData)

        val targetBuffer =
            ByteBuffer.allocateDirect(targetBufferCapacity).order(ByteOrder.LITTLE_ENDIAN)

        val targetSampleCount = processAudioFrame(sourceBuffer, sourceSampleCount, targetBuffer)
        val targetBufferSize =
            targetSampleCount * BYTES_PER_SAMPLE * configuration.outputChannel.count

        targetBuffer.rewind()
        targetBuffer.limit(targetBufferSize)
        targetBuffer.position(0)

        val targetData = ByteArray(targetBufferSize)
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
        targetBuffer: ByteBuffer
    ): Int

    private external fun releaseProcessor()

    companion object {
        init {
            System.loadLibrary("androidresampler")
        }
    }
}