package com.example.praktikdrhfanina.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.praktikdrhfanina.databinding.ItemPatientBinding
import com.example.praktikdrhfanina.model.User
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class PatientAdapter(
    private var patients: List<User>,
    private val onEditClick: (User) -> Unit,
    private val onDeleteClick: (User) -> Unit,
    private val onInfoClick: (User) -> Unit
) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    inner class PatientViewHolder(val binding: ItemPatientBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(patient: User) {
            binding.apply {
                tvPatientName.text = patient.username
                tvPhoneNumber.text = patient.phoneNumber
                tvEmail.text = patient.email

                tvCreatedDate.text = formatTanggal(patient.createdAt)

                btnEdit.setOnClickListener { onEditClick(patient) }
                btnDelete.setOnClickListener { onDeleteClick(patient) }
                btnInfo.setOnClickListener { onInfoClick(patient) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = ItemPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(patients[position])
    }

    override fun getItemCount() = patients.size

    fun updateData(newPatients: List<User>) {
        patients = newPatients
        notifyDataSetChanged()
    }

    private fun formatTanggal(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "-"

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Server pake waktu UTC

            val date = inputFormat.parse(dateString)

            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            outputFormat.format(date!!)
        } catch (e: Exception) {
            try {
                dateString.substring(0, 10)
            } catch (e2: Exception) {
                dateString
            }
        }
    }
}