package com.example.dementiaDetectorApp.viewModels
import android.accounts.Account
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dementiaDetectorApp.api.auth.AuthRepository
import com.example.dementiaDetectorApp.api.auth.AuthResult
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
    //Api related vars
    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResults = resultChannel.receiveAsFlow()

    var loginMsg by mutableStateOf("")
    private var isLoading = false

    init {
        authenticate() //does nothing rn, need fawaz to give me a start up login hook
    }

    //Login + Register vals
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    fun onEmailChange(newEmail: String){_email.value = newEmail}

    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    fun isValidEmail(): Boolean {
        return email.value.matches(emailRegex.toRegex())
    }

    private val _pswd = MutableStateFlow("")
    val pswd: StateFlow<String> = _pswd
    fun onPswdChange(newPswd: String){_pswd.value = newPswd}

    private fun validatePassword(): Boolean {
        val password = pswd.value
        return password.length >= 8 &&                    // Minimum 8 characters required
                password.any { it.isUpperCase() } &&       // At least one uppercase letter
                password.any { it.isLowerCase() } &&       // At least one lowercase letter
                password.any { it.isDigit() } &&           // At least one digit (0-9)
                password.any { !it.isLetterOrDigit() } &&  // At least one special character
                password.none { it.isWhitespace() }        // No spaces or whitespace allowed
    }

    private val _confPswd = MutableStateFlow("")
    val confPswd: StateFlow<String> = _confPswd
    fun onConfPswdChange(newConfPswd: String){_confPswd.value = newConfPswd}

    private val _fName = MutableStateFlow("")
    val fName: StateFlow<String> = _fName
    fun onFNameChange(newFName: String){_fName.value = newFName}

    private val _lName = MutableStateFlow("")
    val lName: StateFlow<String> = _lName
    fun onLNameChange(newLname: String){_lName.value = newLname}

    private val _phoneNum = MutableStateFlow("")
    val phoneNum: StateFlow<String> = _phoneNum
    fun onPhoneNumChange(newNum: String){_phoneNum.value = newNum}

   private fun validateNum(): Boolean {
        val number = phoneNum.value
        return number.length == 10 && number.all { it.isDigit() }
    }

    private val _eircode = MutableStateFlow("")
    val eircode: StateFlow<String> = _eircode
    fun onEircodeChange(newEircode: String){_eircode.value = newEircode}

    private fun validateEircode(): Boolean {
        val eircode = eircode.value.uppercase().replace(" ", "")  // Normalize: uppercase, remove spaces
        return eircode.length == 7 &&                             // Exactly 7 characters total
                eircode.matches("^(?:[AC-FHKNPRTV-Y][0-9]{2}|D6W)[0-9AC-FHKNPRTV-Y]{4}$".toRegex())  // Official Eircode pattern
    }


    private val _clinic = MutableStateFlow(-1)
    val clinic: StateFlow<Int> = _clinic
    fun onClinicChange(newID: Int){_clinic.value = newID}

    //UI vals
    private val _s1Visi = MutableStateFlow(false)
    val s1Visi: StateFlow<Boolean> = _s1Visi
    fun onS1VisiChange(newVisi:Boolean){_s1Visi.value=newVisi}

    fun validateS1(): Boolean{
        return _fName.value!="" &&
        _lName.value!="" &&
        validateNum() &&
        validateEircode()
    }

    private val _s2Visi = MutableStateFlow(false)
    val s2Visi: StateFlow<Boolean> = _s2Visi
    fun onS2VisiChange(newVisi:Boolean){_s2Visi.value=newVisi}

    fun validateS2(): Boolean{
        return _email.value!="" &&
                validatePassword() &&
                _pswd.value == confPswd.value
    }

    private val _s3Visi = MutableStateFlow(true)
    val s3Visi: StateFlow<Boolean> = _s3Visi
    fun onS3VisiChange(newVisi:Boolean){_s3Visi.value=newVisi}

    private val _registered = MutableStateFlow(false)
    val registered: StateFlow<Boolean> = _registered

    //API calls
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
                _registered.value=true
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