package com.example.dementiaDetectorApp.auth

interface AuthRepository {
    suspend fun signUp(username: String, pswd:String): AuthResult<Unit>
    suspend fun signIn(username: String, pswd: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}