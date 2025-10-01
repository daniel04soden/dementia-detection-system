package com.example.dementiaDetectorApp.models

data class User(var accountID: String, var fName: String, var lName:String, var phoneNum: String, var address: Address) {
    val fullName: String get() = "$fName $lName"
}