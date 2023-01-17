package com.bale.estudentattendance.Models

data class Session(
    val session_code:String? = null,
    val expiry_date:String? = null,
    val expiry_Time:String?= null
){
    companion object {
        const val SESSION_CODE = "expiry_Time"
        const val EXPIRY_DATE = "expiry_date"
        const val EXPIRY_TIME = "session_code"
    }
}
