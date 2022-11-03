package com.bale.estudentattendance.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bale.estudentattendance.Models.Unit
import com.bale.estudentattendance.R
import com.google.android.material.textview.MaterialTextView

class AllUnitsAdapter(val ls: MutableList<Unit>):RecyclerView.Adapter<AllUnitsAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(val v:View):RecyclerView.ViewHolder(v){
        val unitName = v.findViewById<MaterialTextView>(R.id.UnitName)
        val modeOfStudy = v.findViewById<MaterialTextView>(R.id.studyMode)
        val campus = v.findViewById<MaterialTextView>(R.id.campus)
        val cohort = v.findViewById<MaterialTextView>(R.id.cohort)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val newView = LayoutInflater.from(parent.context).inflate(R.layout.lecturer_unit,parent,false)
        return ItemViewHolder(newView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = ls[position]
        holder.unitName.text = item.unit_name
        holder.modeOfStudy.text = item.studyMode
        holder.campus.text = item.campus
        holder.cohort.text = item.cohort
    }

    override fun getItemCount(): Int {
        return  ls.size
    }
}