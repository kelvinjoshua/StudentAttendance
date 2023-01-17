package com.bale.estudentattendance.Models

data class Unit(
    val lecturer:String? = null,
    val unit_code:String? = null,
    val unit_name:String? = null,
    val cohort:String? = null,
    val campus:String? = null,
    val studyMode:String? = null
) {
    companion object {
        const val LECTURER_NAME ="lecturer"
        const val UNIT_CODE = "unit_code"
        const val UNIT_NAME = "unit_name"
        const val COHORT = "cohort"
        const val CAMPUS = "campus"
        const val STUDY_MODE = "studyMode"
    }
}
