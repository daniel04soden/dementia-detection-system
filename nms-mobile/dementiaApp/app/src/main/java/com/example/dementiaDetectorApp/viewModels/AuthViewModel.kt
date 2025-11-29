package com.example.dementiaDetectorApp.viewModels
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dementiaDetectorApp.api.auth.AuthRepository
import com.example.dementiaDetectorApp.api.auth.AuthResult
import com.example.dementiaDetectorApp.models.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository
): ViewModel() {
    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResults = resultChannel.receiveAsFlow()

    init {
        authenticate()
    }

    var currentAc: Account? =
        null                   //? means can be null, =null defaults it to null until later changed
        private set                                     //Can be called externally but not altered externally

    //UI Fields
    //Step 1 Email and password
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _pswd = MutableStateFlow("")
    val pswd: StateFlow<String> = _pswd

    private val _confPswd = MutableStateFlow("")
    val confPswd: StateFlow<String> = _confPswd

    // Step 2: Personal Info
    private val _fName = MutableStateFlow("")
    val fName: StateFlow<String> = _fName

    private val _lName = MutableStateFlow("")
    val lName: StateFlow<String> = _lName

    private val _phoneNum = MutableStateFlow("")
    val phoneNum: StateFlow<String> = _phoneNum

    // Step 3: Address Info
    private val _addressOne = MutableStateFlow("")
    val addressOne: StateFlow<String> = _addressOne

    private val _addressTwo = MutableStateFlow("")
    val addressTwo: StateFlow<String> = _addressTwo

    private val _addressThree = MutableStateFlow("")
    val addressThree: StateFlow<String> = _addressThree

    private val _city = MutableStateFlow("")
    val city: StateFlow<String> = _city

    private val _county = MutableStateFlow("")
    val county: StateFlow<String> = _county

    private val _eircode = MutableStateFlow("")
    val eircode: StateFlow<String> = _eircode


    var loginMsg by mutableStateOf("")

    fun isValidPswd(): Boolean {
        if ((pswd.value.length >= 8)) {
            return true
        }
        return false
    }

    fun validateNum(): Boolean {
        if (phoneNum.value.length == 10) {
            return true
        }
        return false
    }

    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    fun isValidEmail(): Boolean {
        return email.value.matches(emailRegex.toRegex())
    }

    //Functions to update values in this file based on view's UI fields
    fun onPswdChange(newPswd: String) {
        _pswd.value = newPswd
    }

    fun onConfPswdChange(newConfPswd: String) {
        _confPswd.value = newConfPswd
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onFNameChange(newFName: String) {
        _fName.value = newFName
    }

    fun onLNameChange(newLname: String) {
        _lName.value = newLname
    }

    fun onPhoneNumChange(newNum: String) {
        _phoneNum.value = newNum
    }

    fun onAddressOneChange(newAddressOne: String) {
        _addressOne.value = newAddressOne
    }

    fun onAddressTwoChange(newAddressTwo: String) {
        _addressTwo.value = newAddressTwo
    }

    fun onAddressThreeChange(newAddressThree: String) {
        _addressThree.value = newAddressThree
    }

    fun onCityChange(newCity: String) {
        _city.value = newCity
    }

    fun onEircodeChange(newEircode: String) {
        _eircode.value = newEircode
    }

    var isLoading=false

    fun signUp(
        onSignIn: () -> Unit
    ){
        viewModelScope.launch{
            isLoading=true
            val result = repository.signUp(
                email =email.value,
                pswd=pswd.value,
                fName = fName.value,
                lName = lName.value,
                phoneNum = phoneNum.value,
                eircode = eircode.value
            )
            if (result is AuthResult.Authorized){
                signIn { onSignIn() }
            }
            resultChannel.send(result)
            isLoading=false
        }
    }

    fun signIn(
        onSignIn: () -> Unit
    ){
        viewModelScope.launch{
            isLoading=true
            val result = repository.signIn(
                email = email.value,
                pswd=pswd.value
            )
            Log.d("Sign in Result", result.toString())
            if (result is AuthResult.Authorized){
                onSignIn()
            }
            resultChannel.send(result)
            isLoading=false
        }
    }

    private fun authenticate(){
        viewModelScope.launch{
            isLoading=true
            val result = repository.authenticate()
            resultChannel.send(result)
            isLoading=false
        }
    }
}