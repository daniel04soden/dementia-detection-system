# This directory contains the test data for the [ADReSS Challenge](https://edin.ac/375QRNI).

The data are in the following directories structure and files:

- Full_wave_enhanced_audio
- Normalised_audio-chunks
- Transcription
- meta_data.txt
- test_results.txt

## Contents

Full wave enhanced audio: contains the audio recordings after noise removal.

Normalised audio-chunks: contains the .wav files which are extracted
from the audio recordings after applying voice activity detection.

transcription: contains the transcription of audio recording along
with meta-data such as age and gender.

meta_data.txt: contain the meta data (i.e. age and gender).

test_results.txt: contains ids of the test subjects. It is the format
of your challenge results. For submitting results for the
classification task, you will need to create a copy of this file,
completing the second column ('Prediction') with your model's
predictions for each instance:

- for the classification task, assign 0 to a non-AD (cc) subject, or 1
  to an AD (cd) subject.

- for the MMSE score prediction task, enter the predicted score. 

Please fill this text file with your results and send it back for
evaluation. You are required to submit all your attempts (up to 5 per
task) together, in separate files. For instance, if you are submitting
results for both tasks (you don't necessarily have to enter both
tasks) you could send:

test_results-classif-1.txt, ..., test_results-classif-classif.txt

and

test_results-regression-1.txt, ..., test_results-regression-5.txt

