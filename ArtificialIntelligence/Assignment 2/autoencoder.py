# -*- coding: utf-8 -*-

#   *** Do not import any library except already imported libraries ***
import numpy as np
import math
import random
#   *** Do not import any library except already imported libraries ***

class AutoEncoder:
    def __init__(self, input_size: int, hidden_size: int, latent_size: int, output_size: int, learning_rate: float):
        '''
        Refer to mlp.py
        '''
        ############################################## EDIT HERE ###########################################
        
        # Xavier 초기화
        xavier_i1 = math.sqrt(1 / (hidden_size + input_size))
        self.W_i1 = np.random.randn(hidden_size, input_size) * xavier_i1
        xavier_i2 = math.sqrt(1 / (latent_size + hidden_size))
        self.W_i2 = np.random.randn(latent_size, hidden_size) * xavier_i2
        xavier_o1 = math.sqrt(1 / (hidden_size + latent_size))
        self.W_o1 = np.random.randn(hidden_size, latent_size) * xavier_o1
        xavier_o2 = math.sqrt(1 / (output_size + hidden_size))
        self.W_o2 = np.random.randn(output_size, hidden_size) * xavier_o2

        self.B_i1 = np.zeros((hidden_size, 1))
        self.B_i2 = np.zeros((latent_size, 1))
        self.B_o1 = np.zeros((hidden_size, 1))
        self.B_o2 = np.zeros((output_size, 1))

        self.grad_W_i1 = np.zeros(self.W_i1.shape)
        self.grad_B_i1 = np.zeros(self.B_i1.shape)
        self.grad_W_i2 = np.zeros(self.W_i2.shape)
        self.grad_B_i2 = np.zeros(self.B_i2.shape)
        self.grad_W_o1 = np.zeros(self.W_o1.shape)
        self.grad_B_o1 = np.zeros(self.B_o1.shape)
        self.grad_W_o2 = np.zeros(self.W_o2.shape)
        self.grad_B_o2 = np.zeros(self.B_o2.shape)

        self.lr = learning_rate
        self.latent = None
    
        ################################################# END ##############################################

    def forward(self, x):
        ############################################## EDIT HERE ###########################################
        
        self.input = np.array(x).reshape((len(x), 1))

        # Input layer -> Hidden layer1
        self.hidden1 = np.dot(self.W_i1, self.input) + self.B_i1
        self.hidden1_active = self.ReLU(self.hidden1)
        
        # Hidden layer1 -> Latent layer
        self.latent = np.dot(self.W_i2, self.hidden1_active) + self.B_i2
        self.latent_active = self.ReLU(self.latent)
        
        # Latent layer -> Hidden layer2
        self.hidden2 = np.dot(self.W_o1, self.latent_active) + self.B_o1
        self.hidden2_active = self.ReLU(self.hidden2)
        
        # Hidden layer2 -> Output layer
        self.output = np.dot(self.W_o2, self.hidden2_active) + self.B_o2        # No activation for the output
        
        return self.output
    
        ################################################# END ##############################################

    def backward(self):
        ############################################## EDIT HERE ###########################################

        # Gradients for Output layer
        error_output = self.output - self.input
        self.grad_W_o2 = np.dot(error_output, self.hidden2_active.T)
        self.grad_B_o2 = np.sum(error_output, axis=1, keepdims=True)
        
        # Backpropagate to Hidden layer2        
        deriv_hidden2 = np.zeros(self.hidden2.shape)
        deriv_hidden2[self.hidden2 > 0] = 1.0        
        error_hidden2 = np.dot(self.W_o2.T, error_output) * deriv_hidden2
        self.grad_W_o1 = np.dot(error_hidden2, self.latent_active.T)
        self.grad_B_o1 = np.sum(error_hidden2, axis=1, keepdims=True)
        
        # Backpropagate to Latent layer
        deriv_latent = np.zeros(self.latent.shape)
        deriv_latent[self.latent > 0] = 1.0  
        error_latent = np.dot(self.W_o1.T, error_hidden2) * deriv_latent
        self.grad_W_i2 = np.dot(error_latent, self.hidden1_active.T)
        self.grad_B_i2 = np.sum(error_latent, axis=1, keepdims=True)
        
        # Backpropagate to Hidden layer1
        deriv_hidden1 = np.zeros(self.hidden1.shape)
        deriv_hidden1[self.hidden1 > 0] = 1.0  
        error_hidden1 = np.dot(self.W_i2.T, error_latent) * deriv_hidden1
        self.grad_W_i1 = np.dot(error_hidden1, self.input.T)
        self.grad_B_i1 = np.sum(error_hidden1, axis=1, keepdims=True)
        
        ################################################# END ##############################################

    def step(self):
        self.W_i1 -= self.lr * self.grad_W_i1
        self.B_i1 -= self.lr * self.grad_B_i1
        self.W_i2 -= self.lr * self.grad_W_i2
        self.B_i2 -= self.lr * self.grad_B_i2
        
        self.W_o1 -= self.lr * self.grad_W_o1
        self.B_o1 -= self.lr * self.grad_B_o1
        self.W_o2 -= self.lr * self.grad_W_o2
        self.B_o2 -= self.lr * self.grad_B_o2

    def ReLU(self, x):
        self.ReLU_mask = np.zeros(x.shape)
        self.ReLU_mask[x >= 0] = 1.0

        return np.multiply(self.ReLU_mask, x)
    
