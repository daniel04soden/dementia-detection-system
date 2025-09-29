# This directory contains the training data for the [ADReSS Challenge](https://edin.ac/375QRNI).

The data are in the following directories structure and files:

- Full_wave_enhanced_audio
   - cc
   - cd
- Normalised_audio-chunks
   - cc
   - cd
- Transcription
   - cc
   - cd
- cc_meta_data.txt
- cd_meta_data.txt 

## Contents

Full wave enhanced audio: contains the audio recordings after noise removal.

Normalised audio-chunks: contains the .wav files which are extracted
from the audio recordings after applying voice activity detection.

transcription: contains the transcription of audio recording along
with meta-data such as age, gender and MMSE score.

For each of these directories, sub-directories cc contain the data of
healthy/control subjects, and cd contains the data of patients with a
diagnostic of Alzheimer's dementia.

cc_meta_data.txt and cd_meta_data.txt: contain the meta data
(i.e. age, gender and MMSE score) of the respective cc and cd data.


