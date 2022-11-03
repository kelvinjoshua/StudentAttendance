package com.bale.estudentattendance

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bale.estudentattendance.Adapters.AllUnitsAdapter
import com.bale.estudentattendance.Adapters.UnitAdapter
import com.bale.estudentattendance.Models.Unit
import com.bale.estudentattendance.databinding.ActivitySubmittedUnitsBinding
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.core.UserData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.toObject


class SubmittedUnits : AppCompatActivity() {

    val db = Firebase.firestore
    private lateinit var adapter:UnitAdapter
    private var campus:String? = null
    private var cohort:String? = null
    private var studyMode:String? = null
    private var units_list = mutableListOf<Unit>()


    private lateinit var binding:ActivitySubmittedUnitsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmittedUnitsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fetchUnitsProgress.visibility = View.VISIBLE
        displayData()
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

    private fun displayData() {
        val accountPreference = getSharedPreferences(Login.PREFERENCE_FILE_NAME, MODE_PRIVATE)
        val lecName = accountPreference.getString(Login.LEC_NAME,"").toString()
        val query = db.collection(COLLECTION_PATH).whereEqualTo("lecturer",lecName)
        adapter = object : UnitAdapter(query,this@SubmittedUnits){
            override fun getItemCount(): Int {
                val r = super.getItemCount()
                if(r>0){
                    binding.placeholder.visibility = View.GONE
                    binding.fetchUnitsProgress.visibility = View.GONE
                }
                else {
                    binding.placeholder.visibility = View.VISIBLE
                    binding.fetchUnitsProgress.visibility = View.GONE
                }
                //Toast.makeText(this@SubmittedUnits,r.toString(),Toast.LENGTH_SHORT).show()
                return r
            }
            /*override fun onDataChanged(){
                if (itemCount == 0){
                    binding.placeholder.visibility = View.VISIBLE
                    binding.fetchUnitsProgress.visibility = View.GONE
                }
                else {
                    binding.placeholder.visibility = View.GONE
                    binding.fetchUnitsProgress.visibility = View.GONE

                }
            }*/
        }
        binding.AllUnitsrecyclerView.adapter = adapter
    }


    private fun toggleUnitCard() {
        binding.materialCardView.visibility = View.GONE
        binding.campusRadio.clearCheck()
        binding.cohortRadio.clearCheck()
        binding.studyModeRadio.clearCheck()

    }

    private fun getUnitDetails() {
        val accountPreference = getSharedPreferences(Login.PREFERENCE_FILE_NAME, MODE_PRIVATE)

        val lecName = accountPreference.getString(Login.LEC_NAME,"").toString()
        val unitName = binding.unitN.text.toString()
        val unitCode = binding.unitC.text.toString()

     if ( unitCode.isEmpty()||unitName.isEmpty() || cohort.isNullOrEmpty() || campus.isNullOrEmpty() || studyMode.isNullOrEmpty()){
            Toast.makeText(this,"All fields required" +" "+ lecName, Toast.LENGTH_LONG).show()
        }
        else {
            val newUnit = Unit(lecName,unitCode,unitName,cohort!!, campus!!, studyMode!!)
            postDetails(newUnit)
        }
    }

    private fun postDetails(newUnit: Unit) {

        db.collection(COLLECTION_PATH)
            .add(newUnit)
            .addOnSuccessListener {
                Toast.makeText(this,"Unit added",Toast.LENGTH_SHORT).show()
                toggleUnitCard()
              //displayData()
                super.onStart()
            }

    }



    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.startListening()
    }
    companion object {
        const val COLLECTION_PATH = "Units by lecturer"
    }
}
