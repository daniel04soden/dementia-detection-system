package com.example.dementiaDetectorApp.api.auth

import android.content.SharedPreferences
import android.util.Log
import retrofit2.HttpException
import java.net.UnknownHostException
import androidx.core.content.edit

class AuthRepoImp(
    private val api: AuthAPI,
    private val prefs: SharedPreferences
): AuthRepository {

    override suspend fun signUp(
        email: String,
        pswd: String,
        fName: String,
        lName: String,
        phoneNum: String,
        eircode: String,
        clinicID: Int
    ): AuthResult<Unit> {
        return try {
            api.signUp(
                request = SignUpRequest(
                    email = email,
                    password = pswd,
                    firstName = fName,
                    lastName = lName,
                    phone = phoneNum,
                    eircode = eircode,
                    clinicID = clinicID
                )
            )
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                Log.d("AuthRepImp", "singUp 401 occurred ${e.message()}")
                AuthResult.Unauthorized()
            } else {
                Log.d("AuthRepImp", "signUp error occurred ${e.message()}")
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Log.d("AuthRepImp", "auth error occurred $e")
            AuthResult.UnknownError()
        }
    }

    override suspend fun signIn(email: String, pswd: String): AuthResult<LoginResponse> {
        return try {
            val response = api.signIn(
                request = LoginRequest(
                    email = email,
                    password = pswd
                )
            )
            prefs.edit {
                putString("jwt", response.token)
            }
            AuthResult.Authorized(response)

        } catch (e: HttpException) {
            if (e.code() == 401) {
                Log.d("AuthRepImp", "singIn 401 occurred ${e.message()}")
                AuthResult.Unauthorized()
            } else {
                Log.d("AuthRepImp", "singIn Http Exception error occurred ${e.message()}")
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Log.d("AuthRepImp", "UnknownHostException: ${e.message}")
            AuthResult.UnknownError()
        }
    }
}