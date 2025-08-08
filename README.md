# Android Resampler

This is a simple Audio Resampler
using [Oboe Resampler](https://github.com/google/oboe/blob/main/src/flowgraph/resampler/README.md#building-the-resampler)
in order to change Channel and SampleRate.

## How to Use

- Add to dependencies

```
implementation("io.github.nailik:androidresampler:0.3")
```

- create the Configuration with input and output settings.

```
val configuration = ResamplerConfiguration(
    quality = ResamplerQuality.BEST,
    inputChannel = ResamplerChannel.STEREO,
    inputSampleRate = 441800,
    outputChannel = ResamplerChannel.MONO,
    outputSampleRate = 16000
)
```

- create the resampler, this also creates the OboeAudioProcessor

```
val resampler = Resampler(configuration)
```

- to convert audio send in a `ByteArray` and you get back a `ByteArray`
- Note: in case of same input and output configuration the input `ByteArray` is just send back
  directly

```
val data: ByteArray = resampler.resample(recordedAudio)
```

- dispose the resampler when finished, note it cannot be used anymore afterwards
- `isDisposed` can be used to check if resampler was disposed already

```
resampler.dispose()
val isDisposed = resampler.isDisposed
```