package com.bale.estudentattendance.Adapters
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bale.estudentattendance.Attendance
import com.bale.estudentattendance.Models.Unit
import com.bale.estudentattendance.R
import com.bale.estudentattendance.databinding.LecturerUnitBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject


open class UnitAdapter (private val query: Query,private val c:Context) :
    FiresToreAdapter<UnitAdapter.ViewHolder>(query){
    private lateinit var unitCode:String
    private lateinit var  unitName:String

    class ViewHolder( val v: View):RecyclerView.ViewHolder(v) {
        val unitName = v.findViewById<MaterialTextView>(R.id.UnitName)
        val modeOfStudy = v.findViewById<MaterialTextView>(R.id.studyMode)
        val campus = v.findViewById<MaterialTextView>(R.id.campus)
        val cohort = v.findViewById<MaterialTextView>(R.id.cohort)
        val unitCard = v.findViewById<MaterialCardView>(R.id.unitCard)


        fun bind(
            snapshot: DocumentSnapshot
        ) {

            val unit = snapshot.toObject<Unit>() ?: return
            unitName.text = unit.unit_name
            modeOfStudy.text = unit.studyMode
            campus.text = unit.campus
            cohort.text = unit.cohort
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.lecturer_unit,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position))
        holder.unitCard.setOnClickListener {
            val intent = Intent(holder.v.context,Attendance::class.java)
            val item = getSnapshot(position)
            intent.putExtra("unit_Name",item.get("unit_name").toString())
            intent.putExtra("unit_code",item.get("unit_code").toString())
            intent.putExtra("cohort",item.get("cohort").toString())
            intent.putExtra("lecturer",item.get("lecturer").toString())
            intent.putExtra("studyMode",item.get("studyMOde").toString())
            holder.v.context.startActivity(intent)
        }
    }
}