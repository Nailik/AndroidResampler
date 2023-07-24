package io.github.nailik.androidresampler_demo.viewmodel

import io.github.nailik.androidresampler_demo.Configuration

data class UiState(
    val configuration: Configuration,
    val isRecording: Boolean,
    val chunksRecorded: Int,
    val chunksConverted: Int
)