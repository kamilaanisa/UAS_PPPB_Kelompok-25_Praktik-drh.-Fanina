package com.example.praktikdrhfanina.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.praktikdrhfanina.databinding.ItemPatientBinding
import com.example.praktikdrhfanina.model.Patient

class PatientAdapter(
    private var patients: List<Patient>,
    private val onEditClick: (Patient) -> Unit,
    private val onDeleteClick: (Patient) -> Unit,
    private val onInfoClick: (Patient) -> Unit
) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    inner class PatientViewHolder(private val binding: ItemPatientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(patient: Patient) {
            binding.apply {
                tvPatientName.text = patient.fullName
                tvPhoneNumber.text = patient.phoneNumber
                tvEmail.text = patient.email
                tvCreatedDate.text = patient.createdDate

                btnEdit.setOnClickListener { onEditClick(patient) }
                btnDelete.setOnClickListener { onDeleteClick(patient) }
                btnInfo.setOnClickListener { onInfoClick(patient) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = ItemPatientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(patients[position])
    }

    override fun getItemCount(): Int = patients.size

    fun updateData(newPatients: List<Patient>) {
        patients = newPatients
        notifyDataSetChanged()
    }
}

