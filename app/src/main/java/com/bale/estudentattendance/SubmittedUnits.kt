package com.bale.estudentattendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bale.estudentattendance.Models.Unit
import com.bale.estudentattendance.databinding.ActivitySubmittedUnitsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SubmittedUnits : AppCompatActivity() {

    val db = Firebase.firestore

    private var campus:String? = null
    private var cohort:String? = null
    private var studyMode:String? = null
    lateinit var lecName:String


    private lateinit var binding:ActivitySubmittedUnitsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmittedUnitsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.add.setOnClickListener {
            binding.materialCardView.visibility = View.VISIBLE
        }
        binding.cancel.setOnClickListener {
            toggleUnitCard()

        }
        binding.submitUnitDetail.setOnClickListener {
            getUnitDetails()
        }
        binding.campusRadio.setOnCheckedChangeListener { radioGroup, i ->
            when(radioGroup.checkedRadioButtonId){
                R.id.kitengela -> {
                    campus = "Kitengela"
                }
                R.id.Kisumu -> {
                    campus = "Kisumu"
                }
                R.id.Main -> {
                    campus = "Main"
                }
            }
        }
        binding.cohortRadio.setOnCheckedChangeListener { radioGroup, i ->
            when(radioGroup.checkedRadioButtonId){
                R.id.bsd -> {
                    cohort = "BSD"
                }
                R.id.Kisumu -> {
                    cohort = "BBIT"

                }
                R.id.Main -> {
                    cohort = "BIT"
                }
            }
        }
        binding.studyModeRadio.setOnCheckedChangeListener { radioGroup, i ->
            when(radioGroup.checkedRadioButtonId){
                R.id.fullTime -> {
                    studyMode = "Full-time"
                }
                R.id.partTime -> {
                    studyMode = "Part-time"
                }

            }
        }
    }

    private fun toggleUnitCard() {
        binding.materialCardView.visibility = View.GONE
        binding.campusRadio.clearCheck()
        binding.cohortRadio.clearCheck()
        binding.studyModeRadio.clearCheck()

    }

    private fun getUnitDetails() {
        val accountPreference = getSharedPreferences(Login.PREFERENCE_FILE_NAME, MODE_PRIVATE)

        lecName = accountPreference.getString(Login.LEC_NAME,"").toString()
        val unitName = binding.unitN.text.toString()
        val unitCode = binding.unitC.text.toString()
        val newUnit = Unit(lecName,unitCode,unitName,cohort!!, campus!!)
        postDetails(newUnit)
      /*  if (lecName!!.isEmpty()|| unitCode.isEmpty() || cohort.isNullOrEmpty() || campus.isNullOrEmpty() || studyMode.isNullOrEmpty()){
            Toast.makeText(this,"All fields required" + lecName, Toast.LENGTH_LONG).show()
        }
        else {
            val newUnit = Unit(lecName,unitCode,unitName,cohort!!, campus!!)
            postDetails(newUnit)
        }*/
    }

    private fun postDetails(newUnit: Unit) {

        db.collection(COLLECTION_PATH)
            .add(newUnit)
            .addOnSuccessListener {
                Toast.makeText(this,"Unit added",Toast.LENGTH_SHORT).show()
                toggleUnitCard()
              //  getDatasize()
            }


    }

    private fun getDatasize() {
        val res = db.collection(COLLECTION_PATH).whereEqualTo("lecturer",lecName)
            .get()
        Toast.makeText(this,res.toString(),Toast.LENGTH_LONG).show()


    }
    companion object {
        const val COLLECTION_PATH = "Units by lecturer"
    }
}