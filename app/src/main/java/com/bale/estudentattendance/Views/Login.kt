package com.bale.estudentattendance.Views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bale.estudentattendance.R
import com.bale.estudentattendance.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var salutation:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkUser()
        auth = Firebase.auth
        binding.login.setOnClickListener {
            signInUser()
        }
        binding.salutationRAdio.setOnCheckedChangeListener { radioGroup, i ->
            when (radioGroup.checkedRadioButtonId){
                R.id.mr -> {
                    salutation = "Mr."
                }
                R.id.mrs -> {
                    salutation = "Mrs."
                }
            }
        }

    }

    private fun signInUser() {
        val email = binding.StudentMail.text.toString()
        val pwd = binding.studentPass.text.toString()
        if(email.isEmpty() ){
            binding.email.setError("Email required")
        }
        else if ( pwd.isEmpty()){
            binding.idinputlayout.setError("Password required")

        }
        else {
            binding.progresss.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    binding.progresss.visibility = View.GONE
                    Toast.makeText(this,"Login successful",Toast.LENGTH_SHORT).show()
                    val res = checkUser()
                    if(!res){
                        binding.loginCard.visibility = View.GONE
                        showAndGetDetails()
                    }
                    else {
                        toNext()
                    }

                }
                else {
                    Toast.makeText(baseContext, "Authentication failed",
                        Toast.LENGTH_SHORT).show()
                }
            }


        }
    }

    private fun showAndGetDetails() {
        binding.lecDetail.visibility = View.VISIBLE
        binding.nameSubmit.setOnClickListener {
            getDetails()
        }
    }

    private fun getDetails() {

        val nameRecieved = binding.name.text.toString()
        Toast.makeText(this,salutation + nameRecieved,Toast.LENGTH_SHORT).show()

       if(nameRecieved.isEmpty() || salutation.isNullOrEmpty()){
            Toast.makeText(this,"All fields required",Toast.LENGTH_SHORT).show()
        }
       else {
            val joined = salutation + nameRecieved

            saveToprefs(joined)
        }
    }

    private fun saveToprefs(joined: String) {
        val lPref = getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
        val prefEditor: SharedPreferences.Editor = lPref.edit()
        with(prefEditor){
            putString(LEC_NAME,joined)
            apply()
        }
        toNext()
    }

    private fun toNext() {
        val intent = Intent(this, SubmittedUnits::class.java)
        startActivity(intent)
    }

    private fun checkUser() : Boolean {
        val accountPreference = getSharedPreferences(PREFERENCE_FILE_NAME, MODE_PRIVATE)

        if(accountPreference.getString(LEC_NAME,"").isNullOrEmpty()){
            binding.registerUser.visibility = View.GONE
            return false
        }
        else {
            return true
        }
    }
    companion object {
        const val PREFERENCE_FILE_NAME = "Account information"
        const val LEC_NAME = "lecturer name"

    }
}