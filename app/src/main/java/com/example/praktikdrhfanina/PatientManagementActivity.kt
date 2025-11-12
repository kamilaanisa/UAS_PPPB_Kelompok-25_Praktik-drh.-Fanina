package com.example.praktikdrhfanina

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.praktikdrhfanina.adapter.PatientAdapter
import com.example.praktikdrhfanina.databinding.ActivityPatientManagementBinding
import com.example.praktikdrhfanina.databinding.DialogAddPatientBinding
import com.example.praktikdrhfanina.databinding.DialogDeletePatientBinding
import com.example.praktikdrhfanina.databinding.DialogEditPatientBinding
import com.example.praktikdrhfanina.databinding.DialogPatientPetsBinding
import com.example.praktikdrhfanina.model.Patient
import com.example.praktikdrhfanina.model.Pet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class PatientManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientManagementBinding
    private lateinit var adapter: PatientAdapter
    private val patientList = mutableListOf<Patient>()
    private var filteredList = mutableListOf<Patient>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        loadSampleData()
    }

    private fun setupRecyclerView() {
        adapter = PatientAdapter(
            patients = filteredList,
            onEditClick = { patient -> showEditDialog(patient) },
            onDeleteClick = { patient -> showDeleteDialog(patient) },
            onInfoClick = { patient -> showPetDetailsDialog(patient) }
        )

        binding.rvPatients.layoutManager = LinearLayoutManager(this)
        binding.rvPatients.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnAddPatient.setOnClickListener {
            showAddPatientDialog()
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterPatients(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadSampleData() {
        // Sample data matching the mockup
        patientList.clear()
        patientList.addAll(
            listOf(
                Patient(
                    id = "1",
                    fullName = "Budi Santoso",
                    phoneNumber = "081234567890",
                    email = "budi@email.com",
                    password = "password123",
                    createdDate = "10/1/2025",
                    pets = listOf(
                        Pet("1", "Rakai", "Kucing"),
                        Pet("2", "Rio", "Anjing"),
                        Pet("3", "Prihastomo", "Kelinci")
                    )
                ),
                Patient(
                    id = "2",
                    fullName = "Siti Nurhaliza",
                    phoneNumber = "081234567891",
                    email = "siti@email.com",
                    password = "password123",
                    createdDate = "15/2/2025",
                    pets = emptyList()
                ),
                Patient(
                    id = "3",
                    fullName = "Ahmad Dahlan",
                    phoneNumber = "081234567892",
                    email = "ahmad@email.com",
                    password = "password123",
                    createdDate = "20/3/2025",
                    pets = emptyList()
                ),
                Patient(
                    id = "4",
                    fullName = "Dewi Lestari",
                    phoneNumber = "081234567893",
                    email = "dewi@email.com",
                    password = "password123",
                    createdDate = "25/4/2025",
                    pets = emptyList()
                )
            )
        )
        filteredList.clear()
        filteredList.addAll(patientList)
        updateUI()
    }

    private fun filterPatients(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(patientList)
        } else {
            filteredList.addAll(
                patientList.filter {
                    it.fullName.contains(query, ignoreCase = true) ||
                            it.email.contains(query, ignoreCase = true) ||
                            it.phoneNumber.contains(query, ignoreCase = true)
                }
            )
        }
        updateUI()
    }

    private fun updateUI() {
        if (filteredList.isEmpty()) {
            binding.rvPatients.visibility = View.GONE
            binding.llEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvPatients.visibility = View.VISIBLE
            binding.llEmptyState.visibility = View.GONE
        }
        adapter.updateData(filteredList)
    }

    private fun showAddPatientDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogAddPatientBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        dialogBinding.btnCloseDialog.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnSave.setOnClickListener {
            val fullName = dialogBinding.etFullName.text.toString()
            val phoneNumber = dialogBinding.etPhoneNumber.text.toString()
            val email = dialogBinding.etEmail.text.toString()
            val password = dialogBinding.etPassword.text.toString()

            if (validateInput(fullName, phoneNumber, email, password)) {
                val newPatient = Patient(
                    id = UUID.randomUUID().toString(),
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    email = email,
                    password = password,
                    createdDate = getCurrentDate(),
                    pets = emptyList()
                )
                patientList.add(newPatient)
                filterPatients(binding.etSearch.text.toString())
                Toast.makeText(this, "Pasien berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun showEditDialog(patient: Patient) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogEditPatientBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Populate existing data
        dialogBinding.etFullName.setText(patient.fullName)
        dialogBinding.etPhoneNumber.setText(patient.phoneNumber)
        dialogBinding.etEmail.setText(patient.email)

        dialogBinding.btnCloseDialog.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnSave.setOnClickListener {
            val fullName = dialogBinding.etFullName.text.toString()
            val phoneNumber = dialogBinding.etPhoneNumber.text.toString()
            val email = dialogBinding.etEmail.text.toString()

            if (validateInput(fullName, phoneNumber, email, patient.password)) {
                val index = patientList.indexOfFirst { it.id == patient.id }
                if (index != -1) {
                    patientList[index] = patient.copy(
                        fullName = fullName,
                        phoneNumber = phoneNumber,
                        email = email
                    )
                    filterPatients(binding.etSearch.text.toString())
                    Toast.makeText(this, "Pasien berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    private fun showDeleteDialog(patient: Patient) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogDeletePatientBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        dialogBinding.tvDeleteMessage.text =
            "Apakah Anda yakin ingin menghapus pasien \"${patient.fullName}\"?\nTindakan ini tidak dapat dibatalkan."

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnDelete.setOnClickListener {
            patientList.removeIf { it.id == patient.id }
            filterPatients(binding.etSearch.text.toString())
            Toast.makeText(this, "Pasien berhasil dihapus", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showPetDetailsDialog(patient: Patient) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogPatientPetsBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        dialogBinding.tvDialogTitle.text = "Hewan Milik ${patient.fullName}"

        // Clear existing pet list
        dialogBinding.llPetList.removeAllViews()

        // Add pets dynamically
        if (patient.pets.isEmpty()) {
            val textView = TextView(this)
            textView.text = "Belum ada hewan terdaftar"
            textView.setTextColor(resources.getColor(android.R.color.darker_gray, null))
            textView.textSize = 14f
            dialogBinding.llPetList.addView(textView)
        } else {
            patient.pets.forEach { pet ->
                val textView = TextView(this)
                textView.text = "${pet.name} (${pet.type})"
                textView.setTextColor(resources.getColor(R.color.text_primary, null))
                textView.textSize = 16f
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.topMargin = if (patient.pets.indexOf(pet) > 0) 12.dpToPx() else 0
                textView.layoutParams = params
                dialogBinding.llPetList.addView(textView)
            }
        }

        dialogBinding.btnCloseDialog.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun validateInput(
        fullName: String,
        phoneNumber: String,
        email: String,
        password: String
    ): Boolean {
        if (fullName.isEmpty()) {
            Toast.makeText(this, "Nama lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Nomor HP tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}

