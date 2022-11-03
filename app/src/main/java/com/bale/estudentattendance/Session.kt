package com.bale.estudentattendance

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import com.bale.estudentattendance.Models.Session
import com.bale.estudentattendance.databinding.ActivitySessionBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Session : AppCompatActivity() ,TimePickerDialog.OnTimeSetListener{
    private lateinit var binding: ActivitySessionBinding
    private var token:String = "0"
    private var setTime:String="sample"
    lateinit var currentDate:String
    val db = Firebase.firestore

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val current = LocalDateTime.now().format(formatter)
        currentDate = current as String
        binding.submitToken.setOnClickListener {
            getToken()
        }
        binding.timeButton.setOnClickListener {
            setExpiryTime()
        }
        binding.Share.setOnClickListener {
            shareCode()
        }
    }

    private fun shareCode() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, token)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setExpiryTime() {

        val calendar = Calendar.getInstance()
        val timeListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY,hour)
            calendar.set(Calendar.MINUTE,minute)
            val formattedTime = SimpleDateFormat("HH:mm").format(calendar.time)
            setTime = formattedTime
            binding.expiryText.text = "Expiry time:"+ " "+ setTime +" "+"hrs"
            saveSessionDetailstoFirebase()
            clearCode()
            binding.saveProgress.visibility = View.VISIBLE

        }
        TimePickerDialog(this,timeListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show()

       /*if(!(setTime.equals("sample")) && token.isNotEmpty()){
            previewData()
        }*/

    }

    private fun clearCode() {
        binding.sessionLayout.clearFocus()
        binding.SessionCode.text = null
    }

    @SuppressLint("SetTextI18n")
    private fun previewData() {
        binding.saveProgress.visibility = View.VISIBLE
        saveSessionDetailstoFirebase()
    }

    private fun saveSessionDetailstoFirebase() {

        val s = Session(token,currentDate,setTime)
        db.collection("Session codes")
            .add(s)
            .addOnSuccessListener {
                Toast.makeText(this,"Data saved", Toast.LENGTH_SHORT).show()
                showShareButton()
            }

    }

    private fun showShareButton() {
        binding.Share.visibility = View.VISIBLE
        binding.saveProgress.visibility = View.GONE
    }

    private fun getToken() {
       // token = binding.SessionCode.text.toString()
        binding.SessionCode.text.apply {
            if(this.isNullOrEmpty()){
                binding.sessionLayout.error = "Value required"
            }
            else {
                this@Session.token = this.toString()
                binding.expiryTokenDetail.text = "Token: $token"
                //Toast.makeText(this@Session,token,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
    }
}