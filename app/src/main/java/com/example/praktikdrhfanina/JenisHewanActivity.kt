package com.example.praktikdrhfanina

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.praktikdrhfanina.adapter.JenisHewanAdapter
import com.example.praktikdrhfanina.databinding.ActivityJenisHewanBinding
import com.example.praktikdrhfanina.databinding.DialogAddJenisHewanBinding
import com.example.praktikdrhfanina.databinding.DialogDeleteJenisHewanBinding
import com.example.praktikdrhfanina.databinding.DialogEditJenisHewanBinding
import com.example.praktikdrhfanina.model.CreateJenisHewanRequest
import com.example.praktikdrhfanina.model.JenisHewan
import com.example.praktikdrhfanina.model.UpdateJenisHewanRequest
import com.example.praktikdrhfanina.model.User
import com.example.praktikdrhfanina.network.ApiClient
import kotlinx.coroutines.launch
import org.json.JSONObject

class JenisHewanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJenisHewanBinding
    private lateinit var adapter: JenisHewanAdapter
    private val jenisHewanList = mutableListOf<JenisHewan>()
    private var filteredList = mutableListOf<JenisHewan>()
    private val patientList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJenisHewanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        loadPatientsFromApi()
        loadJenisHewanFromApi()
    }

    private fun setupRecyclerView() {
        adapter = JenisHewanAdapter(
            jenisHewanList = filteredList,
            patientList = patientList,
            onEditClick = { jenisHewan -> showEditDialog(jenisHewan) },
            onDeleteClick = { jenisHewan -> showDeleteDialog(jenisHewan) }
        )

        binding.rvJenisHewan.layoutManager = LinearLayoutManager(this)
        binding.rvJenisHewan.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnAddJenisHewan.setOnClickListener {
            showAddJenisHewanDialog()
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterJenisHewan(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadJenisHewanFromApi() {
        val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("TOKEN", null)

        if (token == null) {
            Toast.makeText(this, "Token tidak ditemukan. Silahkan login.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val response = ApiClient.getInstance().getAllJenisHewan("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    jenisHewanList.clear()
                    jenisHewanList.addAll(response.body()!!)
                    filteredList.clear()
                    filteredList.addAll(jenisHewanList)
                    updateUI()
                } else {
                    Toast.makeText(this@JenisHewanActivity, "Gagal mengambil data jenis hewan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@JenisHewanActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadPatientsFromApi() {
        val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("TOKEN", null)

        if (token == null) {
            // no token -> spinner will remain empty
            return
        }

        lifecycleScope.launch {
            try {
                val response = ApiClient.getInstance().getAllPatients("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    patientList.clear()
                    patientList.addAll(response.body()!!)
                    // update adapter so owner names appear when list is already loaded
                    adapter.updatePatients(patientList)
                    adapter.updateData(filteredList)

                    if (patientList.isEmpty()) {
                        Toast.makeText(this@JenisHewanActivity, "Daftar pasien kosong — tambahkan pasien terlebih dahulu untuk mengaitkan pemilik", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                // ignore for now; spinner will simply stay empty
            }
        }
    }

    private fun filterJenisHewan(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(jenisHewanList)
        } else {
            filteredList.addAll(
                jenisHewanList.filter {
                    it.nama_jenis.contains(query, ignoreCase = true)
                }
            )
        }
        updateUI()
    }

    private fun showAddJenisHewanDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogAddJenisHewanBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Setup spinner with patients so user can see/select owner (server's jenis-hewan is global)
        val ownerNames = patientList.map { it.username }
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ownerNames
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spinnerOwner.adapter = spinnerAdapter

        var selectedOwnerNameInDialog = if (patientList.isNotEmpty()) patientList[0].username else ""
        var selectedOwnerIdInDialog: Int? = if (patientList.isNotEmpty()) patientList[0].id else null
        dialogBinding.spinnerOwner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                if (position in patientList.indices) {
                    selectedOwnerNameInDialog = patientList[position].username
                    selectedOwnerIdInDialog = patientList[position].id
                } else {
                    selectedOwnerNameInDialog = ""
                    selectedOwnerIdInDialog = null
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                selectedOwnerIdInDialog = null
            }
        })

        dialogBinding.btnCloseDialog.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnSave.setOnClickListener {
            val jenisHewanText = dialogBinding.etJenisHewan.text.toString().trim()

            if (jenisHewanText.isEmpty()) {
                Toast.makeText(this, "Jenis hewan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (patientList.isEmpty() || selectedOwnerIdInDialog == null) {
                Toast.makeText(this, "Pilih pemilik terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
            val token = sharedPref.getString("TOKEN", null)
            if (token == null) {
                Toast.makeText(this, "Token tidak ditemukan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = ApiClient.getInstance().createJenisHewan(
                        "Bearer $token",
                        CreateJenisHewanRequest(nama_jenis = jenisHewanText, id_pasien = selectedOwnerIdInDialog)
                    )

                    if (response.isSuccessful && response.body() != null) {
                        // Refresh from server to ensure we get the real id (avoid id == 0 issues)
                        loadJenisHewanFromApi()
                        val ownerMsg = if (selectedOwnerNameInDialog.isNotEmpty()) " untuk pemilik $selectedOwnerNameInDialog" else ""
                        Toast.makeText(this@JenisHewanActivity, "Jenis hewan berhasil ditambahkan$ownerMsg", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        val errorString = response.errorBody()?.string()
                        val msg = try {
                            if (errorString != null) {
                                val json = JSONObject(errorString)
                                if (json.has("message")) json.getString("message")
                                else errorString
                            } else "Gagal menambahkan jenis hewan"
                        } catch (e: Exception) { errorString ?: "Gagal menambahkan jenis hewan" }
                        Toast.makeText(this@JenisHewanActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@JenisHewanActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }

    private fun showEditDialog(jenisHewan: JenisHewan) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogEditJenisHewanBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Setup owner spinner
        val ownerNames = patientList.map { it.username }
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ownerNames
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spinnerOwner.adapter = spinnerAdapter

        dialogBinding.etJenisHewan.setText(jenisHewan.nama_jenis)

        var selectedOwnerNameInDialog = if (patientList.isNotEmpty()) patientList[0].username else ""
        var selectedOwnerIdInDialog: Int? = if (patientList.isNotEmpty()) patientList[0].id else null
        dialogBinding.spinnerOwner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                if (position in patientList.indices) {
                    selectedOwnerNameInDialog = patientList[position].username
                    selectedOwnerIdInDialog = patientList[position].id
                } else {
                    selectedOwnerNameInDialog = ""
                    selectedOwnerIdInDialog = null
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                selectedOwnerIdInDialog = null
            }
        })

        dialogBinding.btnCloseDialog.setOnClickListener { dialog.dismiss() }
        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }

        dialogBinding.btnSave.setOnClickListener {
            val jenisHewanText = dialogBinding.etJenisHewan.text.toString().trim()

            if (jenisHewanText.isEmpty()) {
                Toast.makeText(this, "Jenis hewan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (patientList.isEmpty() || selectedOwnerIdInDialog == null) {
                Toast.makeText(this, "Pilih pemilik terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
            val token = sharedPref.getString("TOKEN", null)
            if (token == null) {
                Toast.makeText(this, "Token tidak ditemukan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = ApiClient.getInstance().updateJenisHewan(
                        "Bearer $token",
                        jenisHewan.id,
                        UpdateJenisHewanRequest(nama_jenis = jenisHewanText, id_pasien = selectedOwnerIdInDialog)
                    )

                    if (response.isSuccessful && response.body() != null) {
                        // Refresh list to ensure updated server-side state
                        loadJenisHewanFromApi()
                        val ownerMsg = if (selectedOwnerNameInDialog.isNotEmpty()) " untuk pemilik $selectedOwnerNameInDialog" else ""
                        Toast.makeText(this@JenisHewanActivity, "Jenis hewan berhasil diperbarui$ownerMsg", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        val errorString = response.errorBody()?.string()
                        val msg = try {
                            if (errorString != null) {
                                val json = JSONObject(errorString)
                                if (json.has("message")) json.getString("message")
                                else errorString
                            } else "Gagal memperbarui jenis hewan"
                        } catch (e: Exception) { errorString ?: "Gagal memperbarui jenis hewan" }
                        Toast.makeText(this@JenisHewanActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@JenisHewanActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }

    private fun showDeleteDialog(jenisHewan: JenisHewan) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogDeleteJenisHewanBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        dialogBinding.tvDeleteMessage.text =
            "Apakah Anda yakin ingin menghapus jenis hewan \"${jenisHewan.nama_jenis}\"?\nTindakan ini tidak dapat dibatalkan."

        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }

        dialogBinding.btnDelete.setOnClickListener {
            // Validate id before calling delete to avoid sending id=0
            if (jenisHewan.id <= 0) {
                Toast.makeText(this@JenisHewanActivity, "ID jenis hewan tidak valid. Mohon refresh list dan coba lagi.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
            val token = sharedPref.getString("TOKEN", null)
            if (token == null) {
                Toast.makeText(this, "Token tidak ditemukan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = ApiClient.getInstance().deleteJenisHewan(
                        "Bearer $token",
                        jenisHewan.id
                    )

                    if (response.isSuccessful) {
                        // Refresh from server to ensure consistency
                        loadJenisHewanFromApi()
                        Toast.makeText(this@JenisHewanActivity, "Jenis hewan berhasil dihapus", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        val errorString = response.errorBody()?.string()
                        val msg = try {
                            if (errorString != null) {
                                val json = JSONObject(errorString)
                                if (json.has("message")) json.getString("message")
                                else errorString
                            } else "Gagal menghapus: ${response.code()}"
                        } catch (e: Exception) { errorString ?: "Gagal menghapus: ${response.code()}" }
                        Toast.makeText(this@JenisHewanActivity, msg, Toast.LENGTH_SHORT).show()
                        // if server returned 500, suggest a refresh
                        if (response.code() == 500) {
                            loadJenisHewanFromApi()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@JenisHewanActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }

    private fun updateUI() {
        if (filteredList.isEmpty()) {
            binding.rvJenisHewan.visibility = View.GONE
            binding.llEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvJenisHewan.visibility = View.VISIBLE
            binding.llEmptyState.visibility = View.GONE
        }
        adapter.updateData(filteredList)
    }
}

