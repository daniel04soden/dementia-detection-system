#!/usr/bin/env python3
"""
Script to regenerate the Keras model with tensorflow.keras
"""
import os
os.environ['CUDA_VISIBLE_DEVICES'] = '-1'
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

from model_creation import create_model_and_train

print('Creating and training model...')
model, loss, accuracy = create_model_and_train('data/final_data.csv')
print(f'Test Accuracy: {accuracy}')
print(f'Test Loss: {loss}')

print('Saving model...')
model.save('model_store/lifestylemodel.keras')
print('Model saved successfully!')
