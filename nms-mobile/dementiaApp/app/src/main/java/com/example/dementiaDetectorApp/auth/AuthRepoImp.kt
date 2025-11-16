package com.example.dementiaDetectorApp.auth

import android.content.SharedPreferences
import android.util.Log
import retrofit2.HttpException
import java.net.UnknownHostException

class AuthRepoImp(
    private val api:AuthAPI,
    private val prefs: SharedPreferences
):AuthRepository{

    override suspend fun signUp(username: String, pswd: String): AuthResult<Unit> {
       return try{
           api.signUp(
               request = AuthRequest(
                   email = username,
                   pswd = pswd
               )
           )
           signIn(username, pswd)
       }catch (e: HttpException){
           if(e.code()==401){
               Log.d("AuthRepImp","singUp 401 occurred ${e.message()}")
               AuthResult.Unauthorized()
           }else{
               Log.d("AuthRepImp","singUp error occurred ${e.message()}")
               AuthResult.UnknownError()
           }
       }catch(e: Exception){
           Log.d("AuthRepImp","auth error occurred $e")
            AuthResult.UnknownError()
       }
    }

    override suspend fun signIn(username: String, pswd: String): AuthResult<Unit> {
        return try{
            val response = api.signIn(
                request = AuthRequest(
                    email=username,
                    pswd=pswd
                )
            )
            prefs.edit()
                .putString("jwt", response.token)
                .apply()
            AuthResult.Authorized()

        }
        catch (e: HttpException){
            if(e.code()==401){
                Log.d("AuthRepImp","singIn 401 occurred ${e.message()}")
                AuthResult.Unauthorized()
            }else{
                Log.d("AuthRepImp","singIn Http Exception error occurred ${e.message()}")
                AuthResult.UnknownError()
            }
        }catch(e: Exception){
            when (e){
                is UnknownHostException -> Log.d("AuthRepImp", "UnknownHostException: ${e.message}")
            }
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try{
            val token = prefs.getString("jwt", null)?: return AuthResult.Unauthorized()
            api.authenticate("Bearer $token")
            AuthResult.Authorized()

        }catch (e: HttpException){
            if(e.code()==401) {
                Log.d("AuthRepImp","auth 401 occurred ${e.message()}")
                AuthResult.Unauthorized()
            }else{
                Log.d("AuthRepImp","auth error occurred ${e.message()}")
                AuthResult.UnknownError()
            }
        }catch(e: Exception) {
            Log.d("AuthRepImp","auth error occurred $e")
            AuthResult.UnknownError()
        }
    }
}