package com.bale.estudentattendance.Views

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.bale.estudentattendance.Adapters.AttendanceAdapter
import com.bale.estudentattendance.Views.Login.Companion.PREFERENCE_FILE_NAME
import com.bale.estudentattendance.Models.Presence
import com.bale.estudentattendance.databinding.ActivityAttendanceBinding
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Attendance : AppCompatActivity() {
    val db = Firebase.firestore
    lateinit var d:String
   lateinit var a:AttendanceAdapter
   lateinit var exportList:List<Presence>
    private lateinit var binding:ActivityAttendanceBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
         d = LocalDateTime.now().format(formatter)
        binding.todaydate.text = d
        binding.export.setOnClickListener {
            exportAttendance()
        }
        binding.fetchAttendanceProgress.visibility = View.VISIBLE
        getAllRegisteredUnits()
    }

    private fun exportAttendance() {
        val csvFile = generateFile(applicationContext, "Register.csv")
        if (csvFile != null) {
            exportLeadsToCsv(csvFile)
            val intent = gotoFile(this, csvFile)
            startActivity(intent)

        }

    }

    private fun generateFile(context: Context, file: String): File? {
        val csvFile = File(context.filesDir, file)
        csvFile.createNewFile()
        return if (csvFile.exists()) {
            csvFile
        } else {
            null
        }
    }

    private fun gotoFile(requireContext: Context, csvFile: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        val contentUri = FileProvider.getUriForFile(
            requireContext,
            "${requireContext.packageName}.fileprovider",
            csvFile
        )
        val mimeType = requireContext.contentResolver.getType((contentUri))
        intent.setDataAndType(contentUri, mimeType)
        intent.flags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        return intent

    }
    private fun exportLeadsToCsv(csvFile: File) {
        val TAG = "ITEM"
        csvWriter().open(csvFile, append = false) {
            writeRow(listOf( "Student Name", "Admission-no", "Entry date","Unit code","Status"))
            for (item in exportList) {
                writeRow(listOf(item.studentName, item.admissionNumber, item.dateOfSubmission, item.unitCode,item.status))
            }
        }
        Toast.makeText(this, "Guest list generated", Toast.LENGTH_LONG).apply {
            show()
            setGravity(Gravity.TOP, 0, 0)
        }
    }

    private fun getAllRegisteredUnits() {
        val accountPreference = getSharedPreferences(PREFERENCE_FILE_NAME, MODE_PRIVATE)
        val lecName = accountPreference.getString(
            Login.LEC_NAME,"")
        val unitCode = intent!!.extras!!.getString("unit_code")

        db.collection(ATTENDANCE_LIST).whereEqualTo("unitCode",unitCode).whereEqualTo("lecturerName",lecName).whereEqualTo("dateOfSubmission",d).get().addOnSuccessListener {
            res ->
            val r = res.toObjects<Presence>()
            if (r.isEmpty()){
                binding.fetchAttendanceProgress.visibility = View.GONE
                binding.placeholder.visibility = View.VISIBLE
            }
            else {

                exportList = r
                a = AttendanceAdapter(r)
                binding.AttendancerecyclerView.apply {
                    adapter = a
                }
                binding.fetchAttendanceProgress.visibility = View.GONE
                binding.placeholder.visibility = View.GONE
            }
        }

       /* db.collection(ATTENDANCE_LIST).whereEqualTo("unitCode",unitCode).whereEqualTo("lecturerName",lecName).whereEqualTo("dateOfSubmission",d).addSnapshotListener { value, error ->

            if(value != null){
                val res = value!!.toObjects<Presence>()
                Log.d("data",res.toString())
                a = AttendanceAdapter(res)
                binding.AttendancerecyclerView.apply {
                    adapter = a
                }
                binding.fetchAttendanceProgress.visibility = View.GONE
                binding.placeholder.visibility = View.GONE
            }
            else {
                binding.fetchAttendanceProgress.visibility = View.GONE
                binding.placeholder.visibility = View.VISIBLE
            }



            /*if (value != null) {
                binding.fetchAttendanceProgress.visibility = View.GONE
                binding.placeholder.visibility = View.GONE
                val res = value.toObjects<Presence>()
                a = AttendanceAdapter(res)
                binding.AttendancerecyclerView.apply {
                    adapter = a
                }
            }
            else {
                binding.fetchAttendanceProgress.visibility = View.GONE
            }*/
        }*/

    }
    companion object {
        const val ATTENDANCE_LIST = "Unit attendance"
    }
}