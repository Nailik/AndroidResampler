package io.github.nailik.androidresampler_demo

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.nailik.androidresampler_demo.ui.theme.AndroidResamplerTheme
import io.github.nailik.androidresampler_demo.viewmodel.MainViewModel
import io.github.nailik.androidresampler_demo.viewmodel.UiState
import io.github.nailik.androidresampler_demo.viewmodel.UpdateConfigurationEvent
import io.github.nailik.androidresampler_demo.viewmodel.UpdateConfigurationEvent.SelectInputChannelType
import io.github.nailik.androidresampler_demo.viewmodel.UpdateConfigurationEvent.SelectInputSampleRate
import io.github.nailik.androidresampler_demo.viewmodel.UpdateConfigurationEvent.SelectOutputChannelType
import io.github.nailik.androidresampler_demo.viewmodel.UpdateConfigurationEvent.SelectOutputSampleRate
import io.github.nailik.androidresampler_demo.viewmodel.UpdateConfigurationEvent.SelectQuality
import kotlinx.collections.immutable.ImmutableList

class MainActivity : ComponentActivity() {

    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 0)

        setContent {
            AndroidResamplerTheme {
                Screen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen() {
        val uiState by viewModel.state.collectAsState()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Android Resampler Demo")
                    }
                )
            },
            bottomBar = {
                BottomAppBar {
                    Button(
                        enabled = !uiState.isRecording,
                        onClick = viewModel::playOriginalAudio,
                    ) {
                        Text("Original")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        enabled = !uiState.isRecording,
                        onClick = viewModel::playConvertedAudio,
                    ) {
                        Text("Converted")
                    }
                }
            }
        ) {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {

                Content(uiState = uiState)

            }
        }
    }

    @Composable
    fun Content(uiState: UiState) {

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditConfigurationContent(
                enabled = !uiState.isRecording,
                configuration = uiState.configuration,
                onEvent = viewModel::updateConfiguration
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 16.dp)
            )

            val context = LocalContext.current
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.recordAudio(context) }
            ) {
                Text(if (uiState.isRecording) "stopRecording" else "startRecording")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    @Composable
    fun EditConfigurationContent(
        enabled: Boolean,
        configuration: Configuration,
        onEvent: (UpdateConfigurationEvent) -> Unit
    ) {
        Dropdown(
            title = "quality",
            enabled = enabled,
            items = configuration.qualityOptions,
            selected = configuration.quality,
            onSelect = { onEvent(SelectQuality(it)) }
        )

        Divider()

        Dropdown(
            title = "audioInputChannelType",
            enabled = enabled,
            items = configuration.channelTypeOptions,
            selected = configuration.audioInputChannelType,
            onSelect = { onEvent(SelectInputChannelType(it)) }
        )

        Dropdown(
            title = "audioInputSampleRateType",
            enabled = enabled,
            items = configuration.sampleRateOptions,
            selected = configuration.audioInputSampleRateType,
            onSelect = { onEvent(SelectInputSampleRate(it)) }
        )

        Divider()

        Dropdown(
            title = "audioOutputChannelType",
            enabled = enabled,
            items = configuration.channelTypeOptions,
            selected = configuration.audioOutputChannelType,
            onSelect = { onEvent(SelectOutputChannelType(it)) }
        )

        Dropdown(
            title = "audioOutputSampleRateType",
            enabled = enabled,
            items = configuration.sampleRateOptions,
            selected = configuration.audioOutputSampleRateType,
            onSelect = { onEvent(SelectOutputSampleRate(it)) }
        )

        Divider()
    }

    @Composable
    fun <T> Dropdown(
        title: String,
        enabled: Boolean,
        items: ImmutableList<T>,
        selected: T,
        onSelect: (T) -> Unit,
    ) {
        var expanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { expanded = true },
                enabled = enabled
            ) {
                Text(text = title)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = selected.toString())
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(text = item.toString())
                        },
                        onClick = {
                            onSelect(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }


}