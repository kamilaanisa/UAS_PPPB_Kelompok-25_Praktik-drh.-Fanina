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
import androidx.lifecycle.lifecycleScope
import com.example.praktikdrhfanina.adapter.PatientAdapter
import com.example.praktikdrhfanina.databinding.*
import com.example.praktikdrhfanina.model.CreatePatientRequest
import com.example.praktikdrhfanina.model.Pet
import com.example.praktikdrhfanina.model.UpdatePatientRequest
import com.example.praktikdrhfanina.model.User
import com.example.praktikdrhfanina.network.ApiClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class PatientManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientManagementBinding
    private lateinit var adapter: PatientAdapter
    private val patientList = mutableListOf<User>()
    private var filteredList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        loadPatientsFromApi()
    }

    private fun loadPatientsFromApi() {
        val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("TOKEN", null)

        if (token == null) {
            Toast.makeText(this, "Token tidak ditemukan. Silahkan login kembali.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val response = ApiClient.getInstance().getAllPatients("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    patientList.clear()
                    patientList.addAll(response.body()!!)
                    filterPatients(binding.etSearch.text.toString())
                } else {
                    Toast.makeText(this@PatientManagementActivity, "Gagal mengambil data pasien", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@PatientManagementActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
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
        binding.btnAddPatient.setOnClickListener { showAddPatientDialog() }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterPatients(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun filterPatients(query: String) {
        filteredList.clear()
        if (query.isEmpty()) filteredList.addAll(patientList)
        else filteredList.addAll(
            patientList.filter {
                it.username.contains(query, ignoreCase = true) ||
                        it.email.contains(query, ignoreCase = true) ||
                        it.phoneNumber.contains(query, ignoreCase = true)
            }
        )
        updateUI()
    }

    private fun updateUI() {
        binding.rvPatients.visibility = if (filteredList.isEmpty()) View.GONE else View.VISIBLE
        binding.llEmptyState.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
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

        dialogBinding.btnCloseDialog.setOnClickListener { dialog.dismiss() }
        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }

        dialogBinding.btnSave.setOnClickListener {
            val username = dialogBinding.etFullName.text.toString()
            val phoneNumber = dialogBinding.etPhoneNumber.text.toString()
            val email = dialogBinding.etEmail.text.toString()
            val password = dialogBinding.etPassword.text.toString()

            if (!validateInput(username, phoneNumber, email, password)) return@setOnClickListener

            val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
            val token = sharedPref.getString("TOKEN", null)
            if (token == null) {
                Toast.makeText(this, "Token tidak ditemukan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = ApiClient.getInstance().createPatient(
                        "Bearer $token",
                        CreatePatientRequest(username, phoneNumber, email, password)
                    )

                    if (response.isSuccessful && response.body() != null) {
                        val newPatient: User = response.body()!!
                        patientList.add(newPatient)
                        filterPatients(binding.etSearch.text.toString())
                        Toast.makeText(this@PatientManagementActivity, "Pasien berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this@PatientManagementActivity, "Gagal menambahkan pasien", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@PatientManagementActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }


    private fun showEditDialog(patient: User) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogEditPatientBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Populate existing data
        dialogBinding.etFullName.setText(patient.username)
        dialogBinding.etPhoneNumber.setText(patient.phoneNumber)
        dialogBinding.etEmail.setText(patient.email)

        dialogBinding.btnCloseDialog.setOnClickListener { dialog.dismiss() }
        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }

        dialogBinding.btnSave.setOnClickListener {
            val username = dialogBinding.etFullName.text.toString()
            val phoneNumber = dialogBinding.etPhoneNumber.text.toString()
            val email = dialogBinding.etEmail.text.toString()
            val password: String? = null // optional, jika tidak ingin mengubah password

            if (!validateInput(username, phoneNumber, email, password)) return@setOnClickListener

            val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
            val token = sharedPref.getString("TOKEN", null)
            if (token == null) {
                Toast.makeText(this, "Token tidak ditemukan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = ApiClient.getInstance().updatePatient(
                        "Bearer $token",
                        patient.id,
                        UpdatePatientRequest(username, phoneNumber, email, password)
                    )

                    if (response.isSuccessful && response.body() != null) {
                        val updatedPatient: User = response.body()!!
                        val index = patientList.indexOfFirst { it.id == patient.id }
                        if (index != -1) {
                            patientList[index] = updatedPatient
                            filterPatients(binding.etSearch.text.toString())
                        }
                        Toast.makeText(this@PatientManagementActivity, "Pasien berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this@PatientManagementActivity, "Gagal memperbarui pasien", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@PatientManagementActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }

    private fun showDeleteDialog(patient: User) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogDeletePatientBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        dialogBinding.tvDeleteMessage.text =
            "Apakah Anda yakin ingin menghapus pasien \"${patient.username}\"?\nTindakan ini tidak dapat dibatalkan."

        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        dialogBinding.btnDelete.setOnClickListener {
            patientList.removeIf { it.id == patient.id }
            filterPatients(binding.etSearch.text.toString())
            Toast.makeText(this, "Pasien berhasil dihapus", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showPetDetailsDialog(user: User) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogPatientPetsBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        dialogBinding.tvDialogTitle.text = "Hewan Milik ${user.username}"
        dialogBinding.llPetList.removeAllViews()

        if (user.hewans.isEmpty()) {
            val textView = TextView(this)
            textView.text = "Belum ada hewan terdaftar"
            textView.setTextColor(resources.getColor(android.R.color.darker_gray, null))
            textView.textSize = 14f
            dialogBinding.llPetList.addView(textView)
        } else {
            user.hewans.forEach { pet ->
                val textView = TextView(this)
                textView.text = "${pet.nama_hewan} (${pet.jenis_hewan.nama_jenis})"
                textView.setTextColor(resources.getColor(R.color.text_primary, null))
                textView.textSize = 16f
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.topMargin = if (user.hewans.indexOf(pet) > 0) 12.dpToPx() else 0
                textView.layoutParams = params
                dialogBinding.llPetList.addView(textView)
            }
        }

        dialogBinding.btnCloseDialog.setOnClickListener { dialog.dismiss() }
        dialogBinding.btnClose.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun validateInput(
        username: String,
        phoneNumber: String,
        email: String,
        password: String?
    ): Boolean {
        if (username.isEmpty()) {
            Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
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
        // password bisa optional
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
