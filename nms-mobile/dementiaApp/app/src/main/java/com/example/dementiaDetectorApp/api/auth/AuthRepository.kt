package com.example.dementiaDetectorApp.api.auth

import com.example.dementiaDetectorApp.models.Account

interface AuthRepository {
    suspend fun signUp(email: String, pswd: String, fName: String, lName: String, phoneNum:String, eircode:String): AuthResult<Unit>
    suspend fun signIn(email: String, pswd: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}