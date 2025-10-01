package com.example.dementiaDetectorApp.viewModels
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dementiaDetectorApp.models.Account
import com.example.dementiaDetectorApp.models.Address
import com.example.dementiaDetectorApp.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

class AuthViewModel: ViewModel(){
    var currentAc: Account?= null                   //? means can be null, =null defaults it to null until later changed
    private set                                     //Can be called externally but not altered externally
    var currentUsr: User?= null
    private set

    //UI Fields
    //Step 1 Email and password
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val  _pswd = MutableStateFlow("")
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

    //----------------------------------------------------------------------------------------------------------------
    //Subject to change when implementing DB integration
    private val registeredAccounts = mutableListOf<Account>()
    private val registeredUsers = mutableListOf<User>()
    private val loginAttempts: MutableMap<String, Int> = mutableMapOf()

    fun authUser(email: String, pswd: String,): Boolean{
        var authenticated: Boolean = false
        for(ac in registeredAccounts){
            if((ac.email == email)&&(ac.password==pswd)){
                authenticated=true
                break
            }else if((ac.email == email)&&(ac.password!=pswd)){
                val attempts = loginAttempts[email] ?: 0            //"Elvis operator" If null assign 0, if not null assign value to the variable
                loginAttempts[email] = attempts+1
            }
        }
        return authenticated                                        //Apparently "}return x\n}" causes an error so I need to use an entire line for 1 return. Great.
    }
    //---------------------------------------------------------------------------------------------------------------------

    fun login(email: String, pswd: String){
        if((loginAttempts[email] ?: 0) <= 5){
            if(authUser(email,pswd)){
                for (ac in registeredAccounts){
                    if(ac.email==email){
                        currentAc=ac
                        break
                    }
                }
                for(usr in registeredUsers){
                    if (usr.accountID==currentAc!!.accountID){
                        currentUsr=usr
                    }
                }
                loginMsg="Login successful"
            }else{
                loginMsg="Login failed"
            }
        }else{
            loginMsg="There were too many failed attempts to login\nThe account is currently locked"
        }
    }

    fun logout(){
        currentAc=null
        currentUsr=null
        //Call login screen function here
    }

    fun register(){ //Will pull from the fields defined above as they constantly update
        if((validatePswd())&&(validateEmail())){     //Step 1 Email and password
            //Go to step 2 page                      //Step 2 Personal Info
            //Go to step 3 page                      //Step 3 Address


            val newAc=Account("acID",_email.value,_pswd.value)
            registeredAccounts.add(newAc)
            val newAdd=Address(addressOne.value,addressTwo.value,addressThree.value,city.value,county.value,eircode.value)
            val newUsr=User(newAc.accountID, fName.value,lName.value,phoneNum.value,newAdd)
            registeredUsers.add(newUsr)
        }
    }

    fun validatePswd(): Boolean{
        if((_pswd.value==confPswd.value)&&(pswd.value.length>=8)){return true}
        return false
    }

    fun validateEmail(): Boolean{
        for (ac in registeredAccounts){
            if (ac.email==_email.value){return false
            }
        }
        return true
    }
    //Functions to update values in this file based on view's UI fields
    fun onPswdChange(newPswd: String){_pswd.value = newPswd}
    fun onConfPswdChange(newConfPswd: String){_confPswd.value=newConfPswd}
    fun onEmailChange(newEmail :String){_email.value=newEmail}
    fun onFNameChange(newFName: String){_fName.value=newFName}
    fun onLNameChange(newLname: String){_lName.value=newLname}
    fun onPhoneNumChange(newNum: String){_phoneNum.value=newNum}
    fun onAddressOneChange(newAddressOne: String){_addressOne.value=newAddressOne}
    fun onAddressTwoChange(newAddressTwo: String){_addressTwo.value=newAddressTwo}
    fun onAddressThreeChange(newAddressThree: String){_addressThree.value=newAddressThree}
    fun onCityChange(newCity: String){_city.value=newCity}
    fun onCountyChange(newCounty: String){_county.value=newCounty}
    fun onEircodeChange(newEircode: String){_eircode.value=newEircode}
}