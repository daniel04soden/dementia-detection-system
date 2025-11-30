package com.example.dementiaDetectorApp.api.auth

interface AuthRepository {
    suspend fun signUp(email: String, pswd: String, fName: String, lName: String, phoneNum:String, eircode:String, clinicID: Int): AuthResult<Unit>
    suspend fun signIn(email: String, pswd: String): AuthResult<LoginResponse>
}