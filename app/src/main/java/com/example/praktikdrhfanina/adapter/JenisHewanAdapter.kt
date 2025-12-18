package com.example.praktikdrhfanina.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.praktikdrhfanina.R
import com.example.praktikdrhfanina.databinding.ItemJenisHewanBinding
import com.example.praktikdrhfanina.model.JenisHewan

import com.example.praktikdrhfanina.model.User

class JenisHewanAdapter(
    private var jenisHewanList: List<JenisHewan>,
    private var patientList: List<User>,
    private val onEditClick: (JenisHewan) -> Unit,
    private val onDeleteClick: (JenisHewan) -> Unit
) : RecyclerView.Adapter<JenisHewanAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemJenisHewanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: JenisHewan) {
            // Show jenis hewan name
            binding.tvJenisHewan.text = item.nama_jenis

            // If id is invalid, show an ID-warning placeholder so item still appears
            if (item.id <= 0) {
                binding.tvOwnerName.text = "(ID tidak valid)"
                binding.tvOwnerName.setTextColor(binding.root.resources.getColor(R.color.red, null))
                binding.tvOwnerName.visibility = View.VISIBLE
            } else {
                // Try to find owner name from patient list
                val owner = if (item.id_pasien != null) patientList.find { it.id == item.id_pasien } else null
                if (owner != null) {
                    binding.tvOwnerName.text = owner.username
                    binding.tvOwnerName.setTextColor(binding.root.resources.getColor(R.color.text_primary, null))
                    binding.tvOwnerName.visibility = View.VISIBLE
                } else {
                    // show placeholder so user knows owner is missing
                    binding.tvOwnerName.text = "(Pemilik tidak tersedia)"
                    binding.tvOwnerName.setTextColor(binding.root.resources.getColor(R.color.text_secondary, null))
                    binding.tvOwnerName.visibility = View.VISIBLE
                }
            }

            binding.btnEdit.setOnClickListener { onEditClick(item) }
            binding.btnDelete.setOnClickListener { onDeleteClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemJenisHewanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(jenisHewanList[position])
    }

    override fun getItemCount(): Int = jenisHewanList.size

    fun updateData(newList: List<JenisHewan>) {
        jenisHewanList = newList
        notifyDataSetChanged()
    }

    fun updatePatients(newPatients: List<User>) {
        patientList = newPatients
        notifyDataSetChanged()
    }
}
