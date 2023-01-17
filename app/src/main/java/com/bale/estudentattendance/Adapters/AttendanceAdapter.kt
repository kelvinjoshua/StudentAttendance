package com.bale.estudentattendance.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bale.estudentattendance.Models.Presence
import com.bale.estudentattendance.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class AttendanceAdapter(val ls : List<Presence>):RecyclerView.Adapter<AttendanceAdapter.ItemViewHolder>() {
    class ItemViewHolder( val view: View): RecyclerView.ViewHolder(view){
        val studentName: MaterialTextView = view.findViewById(R.id.studentName)
        val adm: MaterialTextView = view.findViewById(R.id.admission)
        val state: MaterialTextView = view.findViewById(R.id.status)
        val card: MaterialCardView = view.findViewById(R.id.unitCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.student, parent, false)
        )

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = ls[position]
        holder.studentName.text = item.studentName
        holder.adm.text = item.admissionNumber
        holder.state.text = item.status
    }

    override fun getItemCount(): Int {
        return ls.size
    }
}