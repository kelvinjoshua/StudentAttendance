package com.bale.estudentattendance.Models

data class Presence(
    val unitCode:String? = null,
    val lecturerName:String? = null,
    val studentName:String?= null,
    val admissionNumber:String? = null,
    val dateOfSubmission:String? = null,
    val status:String? = null
){
    companion object {
        const val ADMISSION_NUMBER = "admissionNumber"
        const val  DATE_SUBMISSION = "dateOfSubmission"
        const val LEC_NAME = "lecturerName"
        const val STAT = "present"
        const val STUDENT_NAME = "studentName"
        const val UNIT_CODE = "unitCode"

    }
}
